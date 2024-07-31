package newCar.event_page.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import newCar.event_page.entity.event.quiz.Quiz;

import java.time.LocalDate;

@Builder
@Data
public class QuizDTO {


    @NotNull
    private Long id;

    @NotNull
    private Integer winnerCount;

    @NotNull
    private LocalDate postDate;

    @NotEmpty
    private String question;

    @NotEmpty
    private String choice1;

    @NotEmpty
    private String choice2;

    @NotEmpty
    private String choice3;

    @NotEmpty
    private String choice4;

    @NotNull
    private Integer correctAnswer;


    public static QuizDTO toDTO(Quiz quiz)
    {
        return QuizDTO.builder()
                .id(quiz.getId())
                .winnerCount(quiz.getWinnerCount())
                .postDate(quiz.getPostDate())
                .question(quiz.getQuestion())
                .choice1(quiz.getChoice1())
                .choice2(quiz.getChoice2())
                .choice3(quiz.getChoice3())
                .choice4(quiz.getChoice4())
                .correctAnswer(quiz.getCorrectAnswer())
                .build();
    }
}
