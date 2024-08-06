package newCar.event_page.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import newCar.event_page.model.entity.event.racing.PersonalityTest;

import java.util.Arrays;
import java.util.List;

@Builder
@Data
public class PersonalityTestDTO {

    @NotNull
    private Long id;

    @NotEmpty
    private String question;

    @NotEmpty
    private List<Choice> choices;


    @Builder
    @Data
    public static class Choice {

        @NotEmpty
        private String text;

        @NotEmpty
        private List<Score> scores;
    }

    @Builder
    @Data
    public static class Score{
        @NotEmpty
        private String type;

        @NotNull
        private Integer value;
    }


    public static PersonalityTestDTO toDTO(PersonalityTest personalityTest) {
        return PersonalityTestDTO.builder()
                .id(personalityTest.getId())
                .question(personalityTest.getQuestion())
                .choices(Arrays.asList(
                        Choice.builder()
                                .text(personalityTest.getChoice1())
                                .scores(Arrays.asList(
                                        Score.builder().type("pet").value(personalityTest.getChoice1Scores().getPetScore()).build(),
                                        Score.builder().type("travel").value(personalityTest.getChoice1Scores().getTravelScore()).build(),
                                        Score.builder().type("space").value(personalityTest.getChoice1Scores().getSpaceScore()).build(),
                                        Score.builder().type("leisure").value(personalityTest.getChoice1Scores().getLeisureScore()).build()
                                ))
                                .build(),
                        Choice.builder()
                                .text(personalityTest.getChoice2())
                                .scores(Arrays.asList(
                                        Score.builder().type("pet").value(personalityTest.getChoice2Scores().getPetScore()).build(),
                                        Score.builder().type("travel").value(personalityTest.getChoice2Scores().getTravelScore()).build(),
                                        Score.builder().type("space").value(personalityTest.getChoice2Scores().getSpaceScore()).build(),
                                        Score.builder().type("leisure").value(personalityTest.getChoice2Scores().getLeisureScore()).build()
                                ))
                                .build()
                ))
                .build();
    }
}