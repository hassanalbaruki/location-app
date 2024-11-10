package ch.solenix.location.application.service;

import ch.solenix.location.application.model.Event;
import ch.solenix.location.application.model.EventLocation;
import ch.solenix.location.application.model.Position;
import ch.solenix.location.application.repository.EventLocationRepository;
import ch.solenix.location.application.repository.EventRepository;
import ch.solenix.location.application.repository.PositionRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
public class EventLocationServiceTest {

    @Mock
    EventRepository eventRepository;

    @Mock
    PositionRepository positionRepository;

    @Mock
    EventLocationRepository eventLocationRepository;

    @InjectMocks
    private EventService eventService;

    @BeforeEach
    void setUp() {
        List<Event> mockEvents = Arrays.asList(
                new Event("E001", LocalDateTime.parse("2023-10-01T10:15:00"), "System Startup", "Info"),
                new Event("E002", LocalDateTime.parse("2023-10-01T10:35:00"), "Navigation Calibration", "Info")
        );

        List<Position> mockLatitudes = Arrays.asList(
                new Position(1L, LocalDateTime.parse("2023-10-01T10:10:00"), 35.1234, "Latitude"),
                new Position(2L, LocalDateTime.parse("2023-10-01T10:20:00"), 37.3245, "Latitude")
        );

        List<Position> mockLongitudes = Arrays.asList(
                new Position(3L, LocalDateTime.parse("2023-10-01T10:10:00"), -117.2437, "Longitude"),
                new Position(4L, LocalDateTime.parse("2023-10-01T10:20:00"), -115.2437, "Longitude")
        );

        List<EventLocation> mockEventLocations = Arrays.asList(
                new EventLocation(1L, mockEvents.get(0), 35.1234, -117.2437),
                new EventLocation(2L, mockEvents.get(1), 37.3245, -115.2437)
        );

        // Mock the repository methods to return mock data
        when(eventRepository.findAll()).thenReturn(mockEvents);
        when(positionRepository.findPositionByType("Latitude")).thenReturn(mockLatitudes);
        when(positionRepository.findPositionByType("Longitude")).thenReturn(mockLongitudes);
        when(positionRepository.findPositionByTimestampAndType(mockLatitudes.get(0).getTimestamp(), "Longitude")).thenReturn(mockLongitudes.get(0));
        when(positionRepository.findPositionByTimestampAndType(mockLatitudes.get(1).getTimestamp(), "Longitude")).thenReturn(mockLongitudes.get(1));
        when(eventLocationRepository.findAll()).thenReturn(mockEventLocations);
        when(eventLocationRepository.findByEventId("E001")).thenReturn(Optional.ofNullable(mockEventLocations.get(0)));
    }

    @Test
    void testGetEventPositionById() {
        Optional<EventLocation> result = eventService.getEventPositionById("E001");
        assertTrue(result.isPresent());
        assertEquals(35.1234, result.get().getLatitude());
        assertEquals(-117.2437, result.get().getLongitude());
    }

    @Test
    void testGetEventPositionByIdNotExist() {
        Optional<EventLocation> result = eventService.getEventPositionById("E003");
        assertFalse(result.isPresent());
    }

    @Test
    void testGetAllEventsWithPositions() {
        List<EventLocation> eventLocations = eventService.getAllEventsWithPositions();
        assertEquals(2, eventLocations.size());
        assertEquals(35.1234, eventLocations.get(0).getLatitude());
        assertEquals(-117.2437, eventLocations.get(0).getLongitude());
        assertEquals(37.3245, eventLocations.get(1).getLatitude());
        assertEquals(-115.2437, eventLocations.get(1).getLongitude());
    }
}
