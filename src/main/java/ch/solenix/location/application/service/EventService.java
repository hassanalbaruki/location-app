package ch.solenix.location.application.service;

import ch.solenix.location.application.model.EventLocation;
import ch.solenix.location.application.repository.EventLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventLocationRepository eventLocationRepository;

    public Optional<EventLocation> getEventPositionById(String eventId) {
        return eventLocationRepository.findByEventId(eventId);
    }

    public List<EventLocation> getAllEventsWithPositions() {
        return eventLocationRepository.findAll();
    }

}
