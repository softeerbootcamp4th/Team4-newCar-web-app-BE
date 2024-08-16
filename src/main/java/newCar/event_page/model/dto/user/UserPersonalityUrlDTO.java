package newCar.event_page.model.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import newCar.event_page.model.enums.Team;

@Data
@NoArgsConstructor
public class UserPersonalityUrlDTO {

    private Team team;
    private String accessToken;
    private String url;
}
