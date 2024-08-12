package newCar.event_page.repository.jpa;

import newCar.event_page.model.entity.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository  extends JpaRepository<Event,Long> {
}
