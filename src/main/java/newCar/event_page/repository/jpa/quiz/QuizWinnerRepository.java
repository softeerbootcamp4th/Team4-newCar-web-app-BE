package newCar.event_page.repository.jpa.quiz;

import newCar.event_page.model.entity.event.quiz.Quiz;
import newCar.event_page.model.entity.event.quiz.QuizWinner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuizWinnerRepository extends JpaRepository<QuizWinner,Long> {

    Optional<QuizWinner> findByQuiz_IdAndEventUser_Id(Long quizId, Long userId);

    List<QuizWinner> findAllByOrderByQuiz_Id();

}
