package newCar.event_page.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AdministratorDTO {

    @NotEmpty
    private final String adminId;

    @NotEmpty
    private final String password;
}
