package newCar.event_page.repository.quiz;

import newCar.event_page.entity.event.quiz.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface QuizRepository extends JpaRepository<Quiz,Long> {
}
