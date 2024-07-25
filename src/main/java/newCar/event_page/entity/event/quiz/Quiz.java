package newCar.event_page.entity.event.quiz;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Getter
public class Quiz {
    @Id
    private Long id;

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
