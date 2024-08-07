package newCar.event_page.repository.racing;

import newCar.event_page.model.entity.event.racing.RacingWinner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface RacingWinnerRepository extends JpaRepository<RacingWinner,Long> {

    @Modifying
    @Query(value = "delete from racing_winner where event_id = :eventId", nativeQuery = true)
    int deleteByEventId(@Param("eventId") Long eventId);

    @Query(value = "select * from racing_winner where event_id = :eventId" , nativeQuery = true)
    List<RacingWinner> findByEventId(@Param("eventId") Long eventId);

}
