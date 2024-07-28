package newCar.event_page.entity.event.racing;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import newCar.event_page.entity.TeamScore;

@Entity
@Getter
@Setter
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

    public void setChoice1Scores(int pet,int travel,int space,int leisure)
    {
        this.choice1Scores = new TeamScore(pet,travel,space,leisure);
    }

    public void setChoice2Scores(int pet,int travel,int space,int leisure)
    {
        this.choice2Scores = new TeamScore(pet,travel,space,leisure);
    }
}
