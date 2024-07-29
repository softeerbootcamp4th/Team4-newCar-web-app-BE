package newCar.event_page.dto;

import lombok.Builder;
import lombok.Data;
import newCar.event_page.entity.event.racing.PersonalityTest;

@Builder
@Data
public class PersonalityTestDTO {
    private Long id;

    private String question;

    private String choice1;
    private String choice2;

    private int choice1_pet_score;
    private int choice1_travel_score;
    private int choice1_space_score;
    private int choice1_leisure_score;

    private int choice2_pet_score;
    private int choice2_travel_score;
    private int choice2_space_score;
    private int choice2_leisure_score;


    public PersonalityTestDTO(Long id, String question, String choice1, String choice2, int choice1_pet_score,
                              int choice1_travel_score, int choice1_space_score, int choice1_leisure_score, int choice2_pet_score,
                              int choice2_travel_score, int choice2_space_score, int choice2_leisure_score) {
        this.id = id;
        this.question = question;
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.choice1_pet_score = choice1_pet_score;
        this.choice1_travel_score = choice1_travel_score;
        this.choice1_space_score = choice1_space_score;
        this.choice1_leisure_score = choice1_leisure_score;
        this.choice2_pet_score = choice2_pet_score;
        this.choice2_travel_score = choice2_travel_score;
        this.choice2_space_score = choice2_space_score;
        this.choice2_leisure_score = choice2_leisure_score;
    }

    public static PersonalityTestDTO toDTO(PersonalityTest personalityTest)
    {
        return PersonalityTestDTO.builder()
                .id(personalityTest.getId())
                .question(personalityTest.getQuestion())
                .choice1(personalityTest.getChoice1())
                .choice2(personalityTest.getChoice2())
                .choice1_pet_score(personalityTest.getChoice1Scores().getPetScore())
                .choice1_travel_score(personalityTest.getChoice1Scores().getTravelScore())
                .choice1_space_score(personalityTest.getChoice1Scores().getSpaceScore())
                .choice1_leisure_score(personalityTest.getChoice1Scores().getLeisureScore())
                .choice2_pet_score(personalityTest.getChoice2Scores().getPetScore())
                .choice2_travel_score(personalityTest.getChoice2Scores().getTravelScore())
                .choice2_space_score(personalityTest.getChoice2Scores().getSpaceScore())
                .choice2_leisure_score(personalityTest.getChoice2Scores().getLeisureScore())
                .build();
    }
}