package newCar.event_page.model.dto.user;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UserQuizAnswerDTO {

    @NotNull
    private Integer answer;
}
