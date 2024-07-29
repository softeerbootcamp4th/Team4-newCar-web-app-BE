package newCar.event_page.repository.racing;

import newCar.event_page.entity.event.racing.PersonalityTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalityTestRepository extends JpaRepository<PersonalityTest, Long> {
}
