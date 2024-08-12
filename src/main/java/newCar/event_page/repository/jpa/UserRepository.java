package newCar.event_page.repository.jpa;

import newCar.event_page.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
