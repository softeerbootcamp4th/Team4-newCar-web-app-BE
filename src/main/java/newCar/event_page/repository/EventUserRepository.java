package newCar.event_page.repository;

import newCar.event_page.entity.event.EventUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventUserRepository extends JpaRepository<EventUser,Long> {
    @Query(value = "select * from event_user where event_id = 1", nativeQuery = true)
    List<EventUser> findByEventId();
}
