package newCar.event_page.entity.event.racing;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import newCar.event_page.dto.PersonalityTestDTO;
import newCar.event_page.entity.Team;
import newCar.event_page.entity.TeamScore;

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

    public void update(PersonalityTestDTO personalityTestDTO)
    {
        this.question = personalityTestDTO.getQuestion();
        this.choice1= personalityTestDTO.getChoice1();
        this.choice2=personalityTestDTO.getChoice2();
        this.choice1Scores = new TeamScore(personalityTestDTO.getChoice1_pet_score(), personalityTestDTO.getChoice1_travel_score(),
                                            personalityTestDTO.getChoice1_space_score(), personalityTestDTO.getChoice1_leisure_score());
        this.choice2Scores = new TeamScore(personalityTestDTO.getChoice2_pet_score(), personalityTestDTO.getChoice2_travel_score(),
                                            personalityTestDTO.getChoice2_space_score(), personalityTestDTO.getChoice2_leisure_score());
    }
}
