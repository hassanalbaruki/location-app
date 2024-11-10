package ch.solenix.location.application.repository;


import ch.solenix.location.application.model.EventLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventLocationRepository extends JpaRepository<EventLocation, Long> {

    Optional<EventLocation> findByEventId(String eventId);
}
