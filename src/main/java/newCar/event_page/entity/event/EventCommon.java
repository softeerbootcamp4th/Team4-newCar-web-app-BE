package newCar.event_page.entity.event;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

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

    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;
}
