package newCar.event_page.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WinnerSettingDTO {

    @NotNull
    private int rank;

    @NotNull
    private int num;

    public WinnerSettingDTO(int rank , int num) {
        this.rank = rank;
        this.num = num;
    }
}
