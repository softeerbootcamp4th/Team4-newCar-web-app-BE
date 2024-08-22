package newCar.event_page.model.dto.user;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserQuizAnswerDTO {

    @NotNull
    private Integer answer;
}
