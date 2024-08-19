package newCar.event_page.model.dto.user;

import lombok.Builder;
import lombok.Data;
import newCar.event_page.model.entity.User;
import newCar.event_page.model.enums.Team;

@Data
@Builder
public class UserInfoDTO {

    private Long userId;
    private String userName;
    private Team team;
    private String url;

    public static UserInfoDTO toDTO(User user){
        return UserInfoDTO.builder()
                .userId(user.getId())
                .userName(user.getNickName())
                .team(user.getTeam())
                .build();
    }
}
