package newCar.event_page.repository.racing;

import newCar.event_page.entity.event.racing.PersonalityTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonalityTestRepository extends JpaRepository<PersonalityTest, Long> {
    List<PersonalityTest> findAllByOrderByIdAsc();
}
