package newCar.event_page.repository.jpa.quiz;

import newCar.event_page.model.entity.event.quiz.QuizEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizEventRepository extends JpaRepository<QuizEvent,Long> {
}
