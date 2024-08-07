package newCar.event_page.repository.jpa.racing;

import newCar.event_page.model.entity.event.racing.PersonalityTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonalityTestRepository extends JpaRepository<PersonalityTest, Long> {
    List<PersonalityTest> findAllByOrderByIdAsc();
}
