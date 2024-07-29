package newCar.event_page.dto;

import lombok.Data;

@Data
public class WinnerSettingDTO {

    private Long id;
    private int num;

    public WinnerSettingDTO(Long id , int num) {
        this.id=id;
        this.num = num;
    }
}
