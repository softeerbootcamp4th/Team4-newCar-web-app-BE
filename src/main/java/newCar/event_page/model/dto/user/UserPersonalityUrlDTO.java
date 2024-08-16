package newCar.event_page.model.dto.user;

import lombok.Data;
import newCar.event_page.model.enums.Team;

@Data
public class UserPersonalityUrlDTO {

    private Team team;
    private String accessToken;
    private String url;
}
