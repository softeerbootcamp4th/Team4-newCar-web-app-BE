package newCar.event_page.repository.jpa.quiz;

import newCar.event_page.model.entity.event.quiz.QuizWinner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizWinnerRepository extends JpaRepository<QuizWinner,Long> {
}
