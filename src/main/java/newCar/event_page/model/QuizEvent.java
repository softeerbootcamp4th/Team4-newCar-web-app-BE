package newCar.event_page.model;

import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Entity
public class QuizEvent {


    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int numberOfWinners;

    public QuizEvent() {
    }

    @Builder
    public QuizEvent(String description, LocalDateTime startTime, LocalDateTime endTime, int numberOfWinners) {
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.numberOfWinners = numberOfWinners;
    }
}