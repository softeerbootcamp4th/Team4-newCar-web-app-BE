package newCar.event_page.repository.jpa.quiz;

import newCar.event_page.model.entity.event.quiz.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<Quiz,Long> {
    //JPA에서는 메서드 이름 명명규칙을 통해 쿼리를 사용할 수 있는 기능을 제공한다.
    List<Quiz> findAllByOrderByPostDateAsc();

    List<Quiz> findAllByOrderByIdAsc();

    Optional<Quiz> findByPostDate(LocalDate postDate);
}
