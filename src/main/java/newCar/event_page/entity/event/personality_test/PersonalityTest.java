package newCar.event_page.entity.event.personality_test;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import newCar.event_page.entity.TeamScore;
import newCar.event_page.entity.event.RacingEvent;

@Entity
@Getter
public class PersonalityTest {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private RacingEvent racingEvent;

    @NotNull
    private String question;

    @NotNull
    private String choice1;
    @NotNull
    private String choice2;

    @Embedded
    @NotNull
    @AttributeOverrides({
            @AttributeOverride(name = "petScore",
                    column = @Column(name = "choice1_pet_score")),
            @AttributeOverride(name = "travelScore",
                    column = @Column(name = "choice1_travel_score")),
            @AttributeOverride(name = "spaceScore",
                    column = @Column(name = "choice1_space_score")),
            @AttributeOverride(name = "leisureScore",
                    column = @Column(name = "choice1_leisure_score")),
    })
    private TeamScore choice1Scores;

    @Embedded
    @NotNull
    @AttributeOverrides({
            @AttributeOverride(name = "petScore",
                    column = @Column(name = "choice2_pet_score")),
            @AttributeOverride(name = "travelScore",
                    column = @Column(name = "choice2_travel_score")),
            @AttributeOverride(name = "spaceScore",
                    column = @Column(name = "choice2_space_score")),
            @AttributeOverride(name = "leisureScore",
                    column = @Column(name = "choice2_leisure_score")),
    })
    private TeamScore choice2Scores;
}
