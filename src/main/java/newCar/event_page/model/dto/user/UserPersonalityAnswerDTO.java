package newCar.event_page.model.dto.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class UserPersonalityAnswerDTO {

    @NotNull
    private List<Choice> answers;

    @Data
    @Builder
    public static class Choice{

        @NotNull
        private Long id;

        @NotEmpty
        private Integer answer;
    }

}
