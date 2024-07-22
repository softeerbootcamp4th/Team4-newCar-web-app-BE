package newCar.event_page.repository;

import newCar.event_page.model.QuizEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@DataJpaTest
class QuizEventRepositoryTest {



    private QuizEvent quizEvent;

    @Autowired
    private QuizEventRepository quizEventRepository;

    @BeforeEach
    void setUp(){
        quizEvent = QuizEvent.builder()
                .id(1L)
                .description("레이싱 이벤트입니다")
                .startTime(LocalDateTime.parse("2021-01-01T15:39:30"))
                .endTime(LocalDateTime.parse("2021-01-31T16:39:30"))
                .numberOfWinners(100)
                .build();
    }

    @DisplayName("Racing 이벤트 저장 JPA 테스트")
    @Test
    public void saveTest() {
        //given

        //when
        final QuizEvent result = quizEventRepository.save(quizEvent);

        //then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDescription()).isEqualTo("레이싱 이벤트입니다");
        assertThat(result.getStartTime()).isEqualTo(LocalDateTime.parse("2021-01-01T15:39:30"));
        assertThat(result.getEndTime()).isEqualTo(LocalDateTime.parse("2021-01-31T16:39:30"));
        assertThat(result.getNumberOfWinners()).isEqualTo(100);

    }
}