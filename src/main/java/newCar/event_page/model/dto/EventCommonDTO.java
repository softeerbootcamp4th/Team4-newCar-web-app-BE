package newCar.event_page.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import newCar.event_page.model.entity.event.EventCommon;

import java.time.LocalDateTime;

@Builder
@Data
@Schema (description = "common-event 정보")
public class EventCommonDTO {

    @NotEmpty
    @Schema (description = "이벤트명" , example = "캐스퍼 이벤트!!")
    public String eventName;

    @NotEmpty
    @Schema (description = "담당자" , example = "배진환")
    public String managerName;

    @NotNull
    @Schema (description = "이벤트 시작 시간" ,example = "2024-01-31T18:30:00")
    public LocalDateTime startTime;

    @NotNull
    @Schema (description = "이벤트 종료 시간" , example = "2024-02-28T18:30:00")
    public LocalDateTime endTime;



    public static EventCommonDTO toDTO(EventCommon eventCommon){
        return EventCommonDTO.builder()
                .eventName(eventCommon.getEventName())
                .managerName(eventCommon.getManagerName())
                .startTime(eventCommon.getStartTime())
                .endTime(eventCommon.getEndTime())
                .build();
    }
}
