package newCar.event_page.entity.event;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import newCar.event_page.entity.event.personality_test.PersonalityTest;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("R")
@Getter
public class RacingEvent extends Event{

    @OneToMany(mappedBy = "racingEvent", cascade = CascadeType.ALL, orphanRemoval = true)
    List<PersonalityTest> personalityTests = new ArrayList<>();
}
