package ch.solenix.location.application.service;

import ch.solenix.location.application.model.Event;
import ch.solenix.location.application.model.EventLocation;
import ch.solenix.location.application.model.Position;
import ch.solenix.location.application.repository.EventLocationRepository;
import ch.solenix.location.application.repository.EventRepository;
import ch.solenix.location.application.repository.PositionRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DataProcessingService {

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventLocationRepository eventLocationRepository;

    @PostConstruct
    public void loadData() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        //This check is to prevent overwriting data in the database.
        if (positionRepository.count() == 0) {
            try (InputStream inputStream = getClass().getResourceAsStream("/annex/latitudes.json")) {
                List<Position> latitudes = mapper.readValue(inputStream, new TypeReference<>() {
                });
                latitudes = latitudes.stream()
                        .peek(position -> position.setType("Latitude"))
                        .collect(Collectors.toList());

                positionRepository.saveAll(latitudes);
            } catch (IOException e) {
                log.error("Failed to load or process latitudes from JSON file", e);
            }

            try (InputStream inputStream = getClass().getResourceAsStream("/annex/longitudes.json")) {
                List<Position> longitudes = mapper.readValue(inputStream, new TypeReference<>() {
                });
                longitudes = longitudes.stream()
                        .peek(position -> position.setType("Longitude"))
                        .collect(Collectors.toList());
                positionRepository.saveAll(longitudes);
            } catch (IOException e) {
                log.error("Failed to load longitudes from JSON file", e);
            }

            try (InputStream inputStream = getClass().getResourceAsStream("/annex/events.json")) {
                List<Event> events = mapper.readValue(inputStream, new TypeReference<>() {
                });
                eventRepository.saveAll(events);
            } catch (IOException e) {
                log.error("Failed to load events from JSON file", e);
            }
            storeEventsLocations();
        }
    }

    public void storeEventsLocations() {
        List<Event> events = eventRepository.findAll();
        List<Position> latitudes = positionRepository.findPositionByType("Latitude");

        List<EventLocation> eventLocations = events.stream().map(event -> {
            Position closestLatitude = findClosestPosition(latitudes, event.getOccurrenceTime());
            Position closestLongitude = positionRepository.findPositionByTimestampAndType(closestLatitude.getTimestamp(), "Longitude");

            if (closestLongitude == null) {
                return null;
            }
            return new EventLocation(null, event, closestLatitude.getPosition(), closestLongitude.getPosition());
        }).filter(Objects::nonNull).toList();
        eventLocationRepository.saveAll(eventLocations);
    }

    /**
     * This method is used to find the Position object in the list that is closest in time to a specified occurrence time.
     * It compares the absolute time differences between the target time and each position's time,
     * and returns the one with the smallest difference.
     */
    public static Position findClosestPosition(List<Position> positions, LocalDateTime occurrenceDateAndTime) {
        if (positions == null || positions.isEmpty()) {
            return null;
        }
        LocalTime occurrenceTime = occurrenceDateAndTime.toLocalTime();
        Position closestPosition = null;
        Duration shortestDuration = null;
        for (Position pos : positions) {
            LocalTime positionTime = pos.getTimestamp().toLocalTime();
            Duration currentDuration = Duration.between(positionTime, occurrenceTime).abs();

            if (shortestDuration == null || currentDuration.compareTo(shortestDuration) < 0) {
                closestPosition = pos;
                shortestDuration = currentDuration;
                if (shortestDuration.isZero()) {
                    break;
                }
            } else return closestPosition;
        }
        return closestPosition;
    }
}
