package newCar.event_page.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class WinnerSettingDTO {

    @NotNull
    private Integer rank;

    @NotNull
    private Integer num;

}
