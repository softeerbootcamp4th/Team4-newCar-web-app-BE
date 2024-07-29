package newCar.event_page.entity.event;

import jakarta.persistence.*;
import lombok.Getter;

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
}
