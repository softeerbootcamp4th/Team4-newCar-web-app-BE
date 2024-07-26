package newCar.event_page.entity.event.quiz;

import jakarta.persistence.*;
import lombok.Getter;
import newCar.event_page.entity.event.EventUser;

@Entity
@Getter
public class QuizWinner {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name="QUIZ_ID")
    private Quiz quiz;

    @ManyToOne
    @JoinColumn(name="USER_ID")
    private EventUser eventUser;

}
