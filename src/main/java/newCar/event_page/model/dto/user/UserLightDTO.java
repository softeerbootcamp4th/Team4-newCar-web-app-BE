package newCar.event_page.model.dto.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserLightDTO {

    @NotEmpty
    private String userId;

    @NotEmpty
    private String password;
}
