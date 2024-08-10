package newCar.event_page.repository.jpa;

import newCar.event_page.model.entity.UserLight;
import newCar.event_page.model.entity.event.EventUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLightRepository extends JpaRepository<UserLight,Long> {

    Long findByUserId(String userId);
}
