package newCar.event_page.model.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import newCar.event_page.model.entity.event.EventCommon;

import java.time.LocalDateTime;

@Data
@Builder
public class UserEventTimeDTO {

    @NotNull
    public LocalDateTime startTime;

    @NotNull
    public LocalDateTime endTime;

    public static UserEventTimeDTO toDTO(EventCommon eventCommon){
        return UserEventTimeDTO.builder()
                .startTime(eventCommon.getStartTime())
                .endTime(eventCommon.getEndTime())
                .build();
    }
}