package newCar.event_page.repository.jpa;

import newCar.event_page.model.entity.event.EventUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventUserRepository extends JpaRepository<EventUser,Long> {
    EventUser findByUserIdAndEventId(Long userId, Long eventId);

    List<EventUser> findByEventId(Long EventId);
}
