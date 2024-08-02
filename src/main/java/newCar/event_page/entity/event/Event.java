package newCar.event_page.entity.event;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="etype")
public class Event {
    @Id
    @GeneratedValue
    @Column(name="EVENT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="EVENT_COMMON_ID")
    private EventCommon eventCommon;

    public long getDuration(){
        LocalDate startDate = eventCommon.getStartTime().toLocalDate();
        LocalDate endDate = eventCommon.getEndTime().toLocalDate();
        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }
}
