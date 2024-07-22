package newCar.event_page.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Entity //해당 객체가 JPA에서 관리하고 있는 객체인 것을 정의
public class RacingEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int numberOfWinners;

    public RacingEvent() {
    }

    @Builder
    public RacingEvent(Long id, String description, LocalDateTime startTime, LocalDateTime endTime, int numberOfWinners) {
        this.id=id;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.numberOfWinners = numberOfWinners;
    }
}