package ch.solenix.location.application.repository;


import ch.solenix.location.application.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {

    /**
     * a query to get a list of positions filterd by type
     */
    List<Position> findPositionByType(String type);

    Position findPositionByTimestampAndType(LocalDateTime time, String type);
}
