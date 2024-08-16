package newCar.event_page.model.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import newCar.event_page.model.entity.User;

@Data
@Builder
public class UserClickNumberDTO {

    @NotNull
    private Integer clickNumber;


    public static UserClickNumberDTO toDTO(User user){

        return UserClickNumberDTO.builder()
                                .clickNumber(user.getClickNumber())
                                .build();
    }
}
