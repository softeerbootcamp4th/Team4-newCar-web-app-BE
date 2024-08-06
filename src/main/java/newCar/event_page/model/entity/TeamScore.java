package newCar.event_page.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Embeddable
@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class TeamScore {
    private final int petScore;
    private final int travelScore;
    private final int spaceScore;
    private final int leisureScore;
}

