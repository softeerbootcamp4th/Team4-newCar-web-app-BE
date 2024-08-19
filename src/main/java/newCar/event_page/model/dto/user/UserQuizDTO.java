package newCar.event_page.model.dto.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import newCar.event_page.model.entity.event.quiz.Quiz;

import java.util.Arrays;
import java.util.List;

@Data
@Builder
public class UserQuizDTO {

    @NotNull
    private Long id;

    @NotEmpty
    private String question;

    @NotEmpty
    private List<Choice> choices;


    @Data
    @Builder
    public static class Choice{

        @NotNull
        private Integer num;

        @NotEmpty
        private String text;
    }

    public static UserQuizDTO toDTO(Quiz quiz) {
        return UserQuizDTO.builder()
                .id(quiz.getId())
                .question(quiz.getQuestion())
                .choices(Arrays.asList(
                        UserQuizDTO.Choice.builder().num(0).text(quiz.getChoice1()).build(),
                        UserQuizDTO.Choice.builder().num(1).text(quiz.getChoice2()).build(),
                        UserQuizDTO.Choice.builder().num(2).text(quiz.getChoice3()).build(),
                        UserQuizDTO.Choice.builder().num(3).text(quiz.getChoice4()).build()
                ))
                .build();
    }
}