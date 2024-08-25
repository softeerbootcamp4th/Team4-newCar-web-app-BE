package newCar.event_page.model.entity.event.racing;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import newCar.event_page.model.dto.admin.AdminPersonalityTestDTO;
import newCar.event_page.model.entity.TeamScore;

@Entity
@Getter
@Setter
public class PersonalityTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public void update(AdminPersonalityTestDTO personalityTestDTO)
    {
        this.question = personalityTestDTO.getQuestion();
        this.choice1= personalityTestDTO.getChoices().get(0).getText();
        this.choice2=personalityTestDTO.getChoices().get(1).getText();
        this.choice1Scores = new TeamScore(personalityTestDTO.getChoices().get(0).getScores().get(0).getValue(),
                personalityTestDTO.getChoices().get(0).getScores().get(1).getValue(),
                personalityTestDTO.getChoices().get(0).getScores().get(2).getValue(),
                personalityTestDTO.getChoices().get(0).getScores().get(3).getValue());
        this.choice2Scores = new TeamScore(personalityTestDTO.getChoices().get(1).getScores().get(0).getValue(),
                personalityTestDTO.getChoices().get(1).getScores().get(1).getValue(),
                personalityTestDTO.getChoices().get(1).getScores().get(2).getValue(),
                personalityTestDTO.getChoices().get(1).getScores().get(3).getValue());
    }
}
