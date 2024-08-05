package newCar.event_page.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import newCar.event_page.entity.Team;
import newCar.event_page.entity.event.racing.RacingWinner;

@Data
@RequiredArgsConstructor
@Builder
public class RacingWinnersDTO {

    private final Integer rank;
    private final String name;
    private final String phoneNumber;
    private final Integer clickNumber;
    private final Team team;


    public static RacingWinnersDTO toDTO(RacingWinner racingWinner) {

        return RacingWinnersDTO.builder()
                .rank(racingWinner.getRank())
                .name(racingWinner.getEventUser().getUser().getUserName())
                .phoneNumber(racingWinner.getEventUser().getUser().getPhoneNumber())
                .clickNumber(racingWinner.getEventUser().getUser().getClickNumber())
                .team(racingWinner.getEventUser().getUser().getTeam())
                .build();
    }
}
