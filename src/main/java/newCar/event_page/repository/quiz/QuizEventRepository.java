package newCar.event_page.repository.quiz;

import newCar.event_page.entity.event.quiz.QuizEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizEventRepository extends JpaRepository<QuizEvent,Long> {
}
