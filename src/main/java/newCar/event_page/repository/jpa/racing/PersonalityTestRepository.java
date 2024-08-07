package newCar.event_page.repository.jpa.racing;

import newCar.event_page.model.entity.event.racing.PersonalityTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonalityTestRepository extends JpaRepository<PersonalityTest, Long> {
    List<PersonalityTest> findAllByOrderByIdAsc();
}
