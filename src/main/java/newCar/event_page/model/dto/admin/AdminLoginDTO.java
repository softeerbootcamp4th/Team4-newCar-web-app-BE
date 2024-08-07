package newCar.event_page.model.dto.admin;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AdminLoginDTO {

    @NotEmpty
    private String adminId;

    @NotEmpty
    private String password;
}
