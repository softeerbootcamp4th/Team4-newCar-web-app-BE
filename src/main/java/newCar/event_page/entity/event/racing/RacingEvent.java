package newCar.event_page.entity.event.racing;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import newCar.event_page.entity.event.Event;

import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("R")
@Getter
public class RacingEvent extends Event {

    @OneToMany(mappedBy = "racingEvent", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<PersonalityTest> personalityTests = new HashSet<>();
}
