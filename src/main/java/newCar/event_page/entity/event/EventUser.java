package newCar.event_page.entity.event;

import jakarta.persistence.*;
import lombok.Getter;
import newCar.event_page.entity.User;


@Entity
@Getter
public class EventUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "EVENT_ID", foreignKey = @ForeignKey(name = "FK_EVENT_ID"))
    private Event event;
}

