package newCar.event_page.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class WinnerSettingDTO {
    @NotNull
    private final Integer rank;

    @NotNull
    private final Integer num;
}
