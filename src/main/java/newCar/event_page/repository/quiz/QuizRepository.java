package newCar.event_page.repository.quiz;

import newCar.event_page.entity.event.quiz.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;


public interface QuizRepository extends JpaRepository<Quiz,Long> {
}
