package newCar.event_page.repository;

import newCar.event_page.entity.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EventRepository  extends JpaRepository<Event,Long> {
}
