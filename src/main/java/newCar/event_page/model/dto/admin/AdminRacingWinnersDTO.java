package newCar.event_page.model.dto.admin;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import newCar.event_page.model.enums.Team;
import newCar.event_page.model.entity.event.racing.RacingWinner;

@Data
@RequiredArgsConstructor
@Builder
public class AdminRacingWinnersDTO {

    private final Integer rank;
    private final String name;
    private final String phoneNumber;
    private final Integer clickNumber;
    private final Team team;


    public static AdminRacingWinnersDTO toDTO(RacingWinner racingWinner) {

        return AdminRacingWinnersDTO.builder()
                .rank(racingWinner.getRank())
                .name(racingWinner.getEventUser().getUser().getUserName())
                .phoneNumber(racingWinner.getEventUser().getUser().getPhoneNumber())
                .clickNumber(racingWinner.getEventUser().getUser().getClickNumber())
                .team(racingWinner.getEventUser().getUser().getTeam())
                .build();
    }
}
