package newCar.event_page.model.entity.event.quiz;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import newCar.event_page.model.entity.event.EventUser;

@Entity
@Getter
@Setter
public class QuizWinner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="QUIZ_ID")
    private Quiz quiz;

    @ManyToOne
    @JoinColumn(name="USER_ID")
    private EventUser eventUser;

}
