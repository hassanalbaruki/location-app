package ch.solenix.location.application.controller;


import ch.solenix.location.application.model.CustomResponse;
import ch.solenix.location.application.model.EventLocation;
import ch.solenix.location.application.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/event/position")
public class EventController {

    @Autowired
    private EventService eventService;


    /**
     * GET
     * This operation returns an event location by id.
     *
     * @param eventId the id of the required event.
     * @return event location object
     **/
    @GetMapping("/{eventId}")
    public ResponseEntity<?> getEventPositionById(@PathVariable String eventId) {
        Optional<EventLocation> eventWithLocation = eventService.getEventPositionById(eventId);
        if (eventWithLocation.isPresent()) {
            return ResponseEntity.ok(eventWithLocation.get());
        } else {
            CustomResponse errorResponse = new CustomResponse(LocalDateTime.now(), "404", "The event with ID " + eventId + " does not exist.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }


    /**
     * GET
     * This operation returns a list of event locations.
     *
     * @return event positions List
     **/
    @GetMapping("/all")
    public ResponseEntity<?> listEventPositions() {
        final List<EventLocation> allEventPositions = eventService.getAllEventsWithPositions();
        if (allEventPositions.isEmpty()) {
            CustomResponse errorResponse = new CustomResponse(LocalDateTime.now(), "404", "No events exist ");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } else return new ResponseEntity<>(allEventPositions, HttpStatus.OK);
    }
}
