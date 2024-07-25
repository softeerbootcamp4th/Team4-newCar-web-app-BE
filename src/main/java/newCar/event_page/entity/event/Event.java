package newCar.event_page.entity.event;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="etype")
public class Event {
    @Id
    @GeneratedValue
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
