package newCar.event_page.entity.event.racing;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import newCar.event_page.entity.event.EventUser;

@Entity
@Getter
public class RacingWinner {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="EVENT_ID")
    private RacingEvent racingEvent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USER_ID")
    private EventUser eventUser;

    @Column(name = "WINNER_RANK")
    private int rank;

    public RacingWinner(RacingEvent racingEvent,EventUser eventUser,int rank){
        this.racingEvent = racingEvent;
        this.eventUser = eventUser;
        this.rank = rank;
    }
}
