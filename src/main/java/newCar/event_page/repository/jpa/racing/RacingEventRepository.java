package newCar.event_page.repository.racing;

import newCar.event_page.model.entity.event.racing.RacingEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RacingEventRepository extends JpaRepository<RacingEvent,Long> {
}
