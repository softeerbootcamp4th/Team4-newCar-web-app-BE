package newCar.event_page.repository.jpa;

import newCar.event_page.model.entity.event.EventCommon;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EventCommonRepository extends JpaRepository<EventCommon,Long> {
}
