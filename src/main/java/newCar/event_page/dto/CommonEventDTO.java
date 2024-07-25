package newCar.event_page.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import newCar.event_page.eventstatus.EventStatus;

import java.time.LocalDateTime;

@Data
@Schema (description = "common-event 정보")
public class CommonEventDTO {

    @Schema (description = "이벤트명" , example = "캐스퍼 이벤트!!")
    public String eventName;

    @Schema (description = "담당자" , example = "배진환")
    public String eventManager;

    @Schema (description = "이벤트 상태" , example = "INPROGRESS")
    public EventStatus status;

    @Schema (description = "이벤트 시작 시간" ,example = "2024-01-31T18:30:00")
    public LocalDateTime startTime;

    @Schema (description = "이벤트 종료 시간" , example = "2024-02-28T18:30:00")
    public LocalDateTime endTime;



    public CommonEventDTO(String eventName, String eventManager, EventStatus status, LocalDateTime startTime, LocalDateTime endTime) {
        this.eventName = eventName;
        this.eventManager = eventManager;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
