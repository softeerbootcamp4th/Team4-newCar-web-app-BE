package newCar.event_page.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import newCar.event_page.entity.Team;
import newCar.event_page.entity.event.racing.RacingWinner;

@RequiredArgsConstructor
@Data
@Builder
public class RacingWinnersDTO {

    private Integer rank;
    private String name;
    private String phoneNumber;
    private Integer clickNumber;
    private Team team;


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
