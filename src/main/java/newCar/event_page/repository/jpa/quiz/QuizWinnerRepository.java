package newCar.event_page.repository.jpa.quiz;

import newCar.event_page.model.entity.event.quiz.QuizWinner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizWinnerRepository extends JpaRepository<QuizWinner,Long> {

    Optional<QuizWinner> findByQuizIdAndUserId(Long quizId, Long userId);
}
