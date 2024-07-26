package newCar.event_page.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import newCar.event_page.entity.event.Event;
import newCar.event_page.entity.event.EventStatus;

import java.time.LocalDateTime;

@Data
@Schema (description = "common-event 정보")
@Builder
public class CommonEventDTO {

    @Schema (description = "이벤트명" , example = "캐스퍼 이벤트!!")
    private String eventName;

    @Schema (description = "담당자" , example = "배진환")
    private String eventManager;

    @Schema (description = "이벤트 상태" , example = "INPROGRESS")
    private EventStatus status;

    @Schema (description = "이벤트 시작 시간" ,example = "2024-01-31T18:30:00")
    private LocalDateTime startTime;

    @Schema (description = "이벤트 종료 시간" , example = "2024-02-28T18:30:00")
    private LocalDateTime endTime;




    public static CommonEventDTO toDTO(Event event){
        return CommonEventDTO.builder()
                .eventName(event.getEventName())
                .eventManager(event.getManagerName())
                .status(event.getStatus())
                .startTime(event.getStartTime())
                .endTime(event.getEndTime())
                .build();
    }
}
