package newCar.event_page.repository.quiz;

import newCar.event_page.entity.event.quiz.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface QuizRepository extends JpaRepository<Quiz,Long> {
    @Query(value = "select * from Quiz where post_date between :startTime AND :endTime order by post_date" , nativeQuery = true)
    List<Quiz> findAllByDuration(@Param("startTime") LocalDate startTime, @Param("endTime") LocalDate endTime);
}
