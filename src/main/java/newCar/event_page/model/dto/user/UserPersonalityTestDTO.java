package newCar.event_page.model.dto.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import newCar.event_page.model.entity.event.racing.PersonalityTest;

import java.util.Arrays;
import java.util.List;

@Data
@Builder
public class UserPersonalityTestDTO {

    @NotNull
    private Long id;

    @NotEmpty
    private String question;

    @NotEmpty
    private List<String> choices;

    public static UserPersonalityTestDTO toDTO(PersonalityTest personalityTest) {
        return UserPersonalityTestDTO.builder()
                .id(personalityTest.getId())
                .question(personalityTest.getQuestion())
                .choices(Arrays.asList(
                        personalityTest.getChoice1(), // 첫 번째 선택지 추가
                        personalityTest.getChoice2()  // 두 번째 선택지 추가
                ))
                .build();
    }
}