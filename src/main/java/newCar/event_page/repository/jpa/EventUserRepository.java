package newCar.event_page.repository.jpa;

import newCar.event_page.model.entity.event.EventUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventUserRepository extends JpaRepository<EventUser,Long> {
    /*@Query(value = "select * from event_user where event_id = :eventId", nativeQuery = true)
    List<EventUser> findByEventId(@Param("eventId") Long eventId);*/

    EventUser findByUserIdAndEventId(Long userId, Long eventId);

    /*@Query(value = "select * from event_user where user_id = :userId AND event_id = :eventId" , nativeQuery = true)
    EventUser findByUserIdAndEvendId(@Param("userId") Long userId , @Param("eventId") Long eventId);*/

    List<EventUser> findByEventId(Long EventId);

}
