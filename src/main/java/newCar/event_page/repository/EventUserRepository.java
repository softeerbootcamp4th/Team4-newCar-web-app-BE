package newCar.event_page.repository;

import newCar.event_page.entity.event.EventUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventUserRepository extends JpaRepository<EventUser,Long> {
    @Query(value = "select * from event_user where event_id = :eventId", nativeQuery = true)
    List<EventUser> findByEventId(@Param("eventId") Long eventId);

    @Query(value = "select * from event_user where user_id = :userId AND event_id = :eventId" , nativeQuery = true)
    EventUser findByUserIdAndEvendId(@Param("userId") Long userId , @Param("eventId") Long eventId);
}
