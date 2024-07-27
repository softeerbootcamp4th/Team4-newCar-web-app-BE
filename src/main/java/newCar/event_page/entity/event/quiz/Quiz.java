package newCar.event_page.entity.event.quiz;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Quiz {
    @Id
    @GeneratedValue
    @Column(name="QUIZ_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EVENT_ID")
    private QuizEvent quizEvent;

    @NotNull
    private Integer winnerCount;

    @NotNull
    private LocalDate postDate;

    @NotNull
    private String question;

    @NotNull
    private String choice1;
    @NotNull
    private String choice2;
    @NotNull
    private String choice3;
    @NotNull
    private String choice4;

    @NotNull
    private Integer correctAnswer;
}
