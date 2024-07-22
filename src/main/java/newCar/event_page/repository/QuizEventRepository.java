package newCar.event_page.repository;

import newCar.event_page.model.QuizEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizEventRepository extends JpaRepository<QuizEvent,Long> {

}
