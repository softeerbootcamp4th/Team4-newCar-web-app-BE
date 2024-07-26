package newCar.event_page.entity.event.quiz;

import jakarta.persistence.*;
import lombok.Getter;
import newCar.event_page.entity.event.Event;

import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("Q")
@Getter
public class QuizEvent extends Event {

    @OneToMany(mappedBy = "quizEvent", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Quiz> quizzes = new HashSet<>();
}
