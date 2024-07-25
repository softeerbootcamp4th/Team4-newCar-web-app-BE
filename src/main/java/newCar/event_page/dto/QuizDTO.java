package newCar.event_page.dto;


import lombok.Data;

import java.time.LocalDate;

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


    public QuizDTO(Long id, int winnerCount, LocalDate postDate, String question, String choice1, String choice2, String choice3, String choice4, int correctAnswer) {
        this.id = id;
        this.winnerCount = winnerCount;
        this.postDate = postDate;
        this.question = question;
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.choice3 = choice3;
        this.choice4 = choice4;
        this.correctAnswer = correctAnswer;
    }
}
