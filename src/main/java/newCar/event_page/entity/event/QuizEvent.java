package newCar.event_page.entity.event;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@DiscriminatorValue("Q")
@Getter
public class QuizEvent extends Event{
}
