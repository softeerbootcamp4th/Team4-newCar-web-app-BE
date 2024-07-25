package newCar.event_page.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RacingWinnersDTO {

    private String name;
    private String phoneNumber;
    private LocalDate time;

    public RacingWinnersDTO(String name, String phoneNumber, LocalDate time) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.time = time;
    }
}
