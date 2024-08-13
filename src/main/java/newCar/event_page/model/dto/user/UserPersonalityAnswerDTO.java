package newCar.event_page.model.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;



@Data
public class UserPersonalityAnswerDTO {


    private Long id;

    @NotNull
    private Integer answer;


}
