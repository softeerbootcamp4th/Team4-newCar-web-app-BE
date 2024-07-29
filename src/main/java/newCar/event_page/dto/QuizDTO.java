package newCar.event_page.dto;


import lombok.Builder;
import lombok.Data;
import newCar.event_page.entity.event.quiz.Quiz;

import java.time.LocalDate;

@Builder
@Data
public class QuizDTO {

    private Long id;
    private int winnerCount;
    private LocalDate postDate;
    private String question;

    private String choice1;
    private String choice2;
    private String choice3;
    private String choice4;

    private int correctAnswer;


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
