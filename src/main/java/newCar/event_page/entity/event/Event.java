package newCar.event_page.entity.event;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="etype")
@Table(name = "event", indexes = @Index(name = "idx_event_id", columnList = "EVENT_ID"))
public class Event {
    @Id
    @GeneratedValue
    @Column(name="EVENT_ID")
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
