package newCar.event_page.model.entity.event.quiz;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import newCar.event_page.model.dto.admin.AdminQuizDTO;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public void update(AdminQuizDTO quizDTO) {
        this.winnerCount = quizDTO.getWinnerCount();
        this.question=quizDTO.getQuestion();
        this.choice1=quizDTO.getChoices().get(0).getText();
        this.choice2=quizDTO.getChoices().get(1).getText();
        this.choice3=quizDTO.getChoices().get(2).getText();
        this.choice4=quizDTO.getChoices().get(3).getText();
        this.correctAnswer=quizDTO.getCorrectAnswer();
    }

    public static Quiz createDummy(QuizEvent quizEvent){
        Quiz quiz = new Quiz();
        quiz.setPostDate(LocalDate.parse("2000-01-01"));
        quiz.setQuestion("질문을 입력해주세요");
        quiz.setQuizEvent(quizEvent);
        quiz.setCorrectAnswer(1);
        quiz.setChoice1("1번 선택지를 입력하세요");
        quiz.setChoice2("2번 선택지를 입력하세요");
        quiz.setChoice3("3번 선택지를 입력하세요");
        quiz.setChoice4("4번 선택지를 입력하세요");
        quiz.setWinnerCount(100);
        return quiz;
    }
}
