package newCar.event_page.repository;

import newCar.event_page.model.RacingEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RacingEventRepository extends JpaRepository<RacingEvent,Long> {

}
