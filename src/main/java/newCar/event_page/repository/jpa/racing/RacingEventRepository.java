package newCar.event_page.repository.jpa.racing;

import newCar.event_page.model.entity.event.racing.RacingEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RacingEventRepository extends JpaRepository<RacingEvent,Long> {
}
