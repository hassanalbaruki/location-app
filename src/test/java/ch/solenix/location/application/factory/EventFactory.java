package ch.solenix.location.application.factory;

import ch.solenix.location.application.model.CustomResponse;
import ch.solenix.location.application.model.Event;
import ch.solenix.location.application.model.EventLocation;

import java.time.LocalDateTime;

public final class EventFactory {


    /**
     * Create EventLocation for the tests.
     *
     * @return the created EventLocation Object.
     */
    public static EventLocation createEventLocation() {
        return EventLocation.builder()
                .id(1L)
                .event(Event.builder()
                        .id("E001")
                        .severity("Info")
                        .occurrenceTime(LocalDateTime.parse("2023-10-01T10:15:00"))
                        .eventName("System Startup").build())
                .latitude(35.1234)
                .longitude(-117.2437)
                .build();
    }

    /**
     * Create CustomResponse for the tests.
     *
     * @return the created CustomResponse Object when eventLocationId not exist.
     */
    public static CustomResponse createEventLocationForIdNotExist() {
        return CustomResponse.builder()
                .timestamp(LocalDateTime.now())
                .code("404")
                .message("The event with ID E000 does not exist.")
                .build();
    }
}
