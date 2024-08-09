package newCar.event_page.repository.jpa;

import newCar.event_page.model.entity.UserLight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLightRepository extends JpaRepository<UserLight,Long> {

}
