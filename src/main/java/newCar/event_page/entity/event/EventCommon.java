package newCar.event_page.entity.event;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import newCar.event_page.dto.EventCommonDTO;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class EventCommon {
    @Id
    @GeneratedValue
    @Column(name="EVENT_COMMON_ID")
    private Long id;

    @NotNull
    private String eventName;

    @NotNull
    private String managerName;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    public void update(EventCommonDTO eventCommonDTO) {
        this.eventName=eventCommonDTO.getEventName();
        this.managerName=eventCommonDTO.getManagerName();
        this.startTime=eventCommonDTO.getStartTime();
        this.endTime=eventCommonDTO.getEndTime();
    }
}
