package newCar.event_page.model.entity.event;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import newCar.event_page.model.dto.admin.AdminEventCommonDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Setter
@Getter
public class EventCommon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public void update(AdminEventCommonDTO eventCommonDTO) {
        this.eventName=eventCommonDTO.getEventName();
        this.managerName=eventCommonDTO.getManagerName();
        this.startTime=eventCommonDTO.getStartTime();
        this.endTime=eventCommonDTO.getEndTime();
    }

    public long getDuration(){
        LocalDate startDate = startTime.toLocalDate();
        LocalDate endDate = endTime.toLocalDate();
        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }
}
