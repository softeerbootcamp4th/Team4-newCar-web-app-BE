package newCar.event_page.model.dto.admin;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import newCar.event_page.model.entity.event.quiz.Quiz;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;


@Builder
@Data
public class AdminQuizDTO {


    @NotNull
    private Long id;

    @NotNull
    private Integer winnerCount;

    private LocalDate postDate;

    @NotEmpty
    private String question;

    @NotEmpty
    private List<Choice> choices;

    @NotNull
    private Integer correctAnswer;


    @Data
    @Builder
    public static class Choice{

        @NotNull
        private Integer num;

        @NotEmpty
        private String text;
    }

    public static AdminQuizDTO toDTO(Quiz quiz)
    {
        return AdminQuizDTO.builder()
                .id(quiz.getId())
                .winnerCount(quiz.getWinnerCount())
                .postDate(quiz.getPostDate())
                .question(quiz.getQuestion())
                .choices(Arrays.asList(
                        Choice.builder().num(0).text(quiz.getChoice1()).build(),
                        Choice.builder().num(1).text(quiz.getChoice2()).build(),
                        Choice.builder().num(2).text(quiz.getChoice3()).build(),
                        Choice.builder().num(3).text(quiz.getChoice4()).build()
                ))
                .correctAnswer(quiz.getCorrectAnswer())
                .build();
    }
}