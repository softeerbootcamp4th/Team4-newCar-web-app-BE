package newCar.event_page.entity.event.quiz;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import newCar.event_page.dto.QuizDTO;

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

    public void update(QuizDTO quizDTO) {
        this.winnerCount = quizDTO.getWinnerCount();
        this.postDate=quizDTO.getPostDate();
        this.question=quizDTO.getQuestion();
        this.choice1=quizDTO.getChoices().get(0).getText();
        this.choice2=quizDTO.getChoices().get(1).getText();
        this.choice3=quizDTO.getChoices().get(2).getText();
        this.choice4=quizDTO.getChoices().get(3).getText();
        this.correctAnswer=quizDTO.getCorrectAnswer();
    }
}
