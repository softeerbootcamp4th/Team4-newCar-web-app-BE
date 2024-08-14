package newCar.event_page.model.dto.admin;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import newCar.event_page.model.entity.event.quiz.QuizWinner;

import java.time.LocalDate;

@Data
@Builder
public class AdminQuizWinnersDTO {

    @NotNull
    private String name;

    @NotNull
    private String phoneNumber;

    @NotNull
    private LocalDate postDate;

    public static AdminQuizWinnersDTO toDTO(QuizWinner quizWinner) {

        return AdminQuizWinnersDTO.builder()
                .name(quizWinner.getEventUser().getUser().getNickName())
                .phoneNumber(quizWinner.getEventUser().getUser().getPhoneNumber())
                .postDate(quizWinner.getQuiz().getPostDate())
                .build();
    }
}
