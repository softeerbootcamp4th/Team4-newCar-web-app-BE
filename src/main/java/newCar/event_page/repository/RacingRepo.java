package newCar.event_page.repository;

import newCar.event_page.entity.event.racing.RacingEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RacingRepo extends JpaRepository<RacingEvent,Long> {
}
