package newCar.event_page.service;

import newCar.event_page.exception.FCFS.FCFSFinishedException;
import newCar.event_page.exception.FCFS.FCFSNotStartedYet;
import newCar.event_page.exception.UserAlreadyHasTeamException;
import newCar.event_page.exception.UserLoginFailException;
import newCar.event_page.jwt.JwtTokenProvider;
import newCar.event_page.model.dto.user.*;
import newCar.event_page.model.entity.TeamScore;
import newCar.event_page.model.entity.User;
import newCar.event_page.model.entity.UserLight;
import newCar.event_page.model.entity.event.Event;
import newCar.event_page.model.entity.event.EventCommon;
import newCar.event_page.model.entity.event.EventId;
import newCar.event_page.model.entity.event.EventUser;
import newCar.event_page.model.entity.event.quiz.Quiz;
import newCar.event_page.model.entity.event.racing.PersonalityTest;
import newCar.event_page.model.entity.event.racing.RacingWinner;
import newCar.event_page.model.enums.Team;
import newCar.event_page.model.enums.UserQuizStatus;
import newCar.event_page.repository.jpa.*;
import newCar.event_page.repository.jpa.quiz.QuizRepository;
import newCar.event_page.repository.jpa.racing.PersonalityTestRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserLightRepository userLightRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventCommonRepository eventCommonRepository;

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private PersonalityTestRepository personalityTestRepository;

    @Mock
    private EventRepository eventRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("성격 유형 검사 빈 리스트 가져오기_성공")
    public void getPersonalityTest_Success() {
        //when
        ResponseEntity<List<UserPersonalityTestDTO>> response = userService.getPersonalityTestList();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    @DisplayName("오늘의 퀴즈 가져오기_퀴즈 이벤트 자체가 존재하지 않음")
    public void getQuiz_Fail_Event() {
        assertThatThrownBy(() -> userService.getQuiz(EventId.Quiz.getValue()))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("오늘의 퀴즈 가져오기_실패_오늘 날짜에 퀴즈 없음")
    public void getQuiz_Fail_Date() {
        //given
        Event event = mock(Event.class);
        when(eventRepository.findById(any(Long.class))).thenReturn(Optional.of(event));

        // Mocking quizRepository to throw NoSuchElementException for today's date
        when(quizRepository.findByPostDate(any(LocalDate.class)))
                .thenThrow(new NoSuchElementException("오늘 날짜에 해당하는 퀴즈 이벤트가 존재하지 않습니다."));

        //when & then
        assertThatThrownBy(() -> userService.getQuiz(EventId.Quiz.getValue()))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void testGetQuizNotStartedYet() {
        LocalTime now = LocalTime.now(ZoneId.of("Asia/Seoul"));
        if (now.isBefore(LocalTime.of(15, 15))) {
            assertThrows(FCFSNotStartedYet.class, () -> userService.getQuiz(1L));
        }
    }

    @Test
    public void testGetQuizFinished() {
        List<Boolean> availableArray = Arrays.asList(false, true, true);
        userService.setQuizAvailableArray(new ArrayList<>(availableArray));
        assertThrows(FCFSFinishedException.class, () -> userService.getQuiz(1L));
    }

    @Test
    public void testLoginSuccess() {
        UserLightDTO userLightDTO = new UserLightDTO();
        userLightDTO.setUserId("testUser");
        userLightDTO.setPassword("password");

        UserLight userLight = new UserLight();
        userLight.setUserId("testUser");
        userLight.setPassword("password");

        when(userLightRepository.save(any(UserLight.class))).thenReturn(userLight);
        when(userLightRepository.findById(any(Long.class))).thenReturn(Optional.of(userLight));
        when(jwtTokenProvider.generateUserToken(anyString())).thenReturn("dummyToken");

        ResponseEntity<Map<String, String>> response = userService.login(userLightDTO);
        assertThat(response.getBody()).containsKey("accessToken");

        // Verify save method was called once
        verify(userLightRepository, times(1)).save(any(UserLight.class));
    }

    @Test
    public void testLoginFail() {
        UserLightDTO userLightDTO = new UserLightDTO();
        userLightDTO.setUserId("testUser");
        userLightDTO.setPassword("wrongPassword");

        assertThrows(UserLoginFailException.class, () -> userService.login(userLightDTO));
    }

    @Test
    public void testSubmitPersonalityTestSuccess() {
        List<UserPersonalityAnswerDTO> answers = new ArrayList<>();
        UserPersonalityAnswerDTO answer1 = new UserPersonalityAnswerDTO();
        answer1.setAnswer(1);
        UserPersonalityAnswerDTO answer2 = new UserPersonalityAnswerDTO();
        answer2.setAnswer(1);

        answers.add(answer1);
        answers.add(answer2);
        String token = jwtTokenProvider.generateUserToken("testUser");

        ResponseEntity<UserPersonalityUrlDTO> response = userService.submitPersonalityTest(answers, "Bearer " + token);
        assertThat(response.getBody()).isNotNull();

        // Verify save method was called once
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testSubmitPersonalityTestAlreadyHasTeam() {
        User user = userRepository.findByUserName("testUser").orElseThrow();
        user.setTeam(Team.PET);
        userRepository.save(user);

        List<UserPersonalityAnswerDTO> answers = new ArrayList<>();
        UserPersonalityAnswerDTO answer1 = new UserPersonalityAnswerDTO();
        answer1.setAnswer(1);
        UserPersonalityAnswerDTO answer2 = new UserPersonalityAnswerDTO();
        answer2.setAnswer(1);

        answers.add(answer1);
        answers.add(answer2);

        String token = jwtTokenProvider.generateUserToken("testUser");

        assertThrows(UserAlreadyHasTeamException.class, () -> userService.submitPersonalityTest(answers, "Bearer " + token));
    }

    @Test
    public void testSubmitQuizSuccess() {
        UserQuizAnswerDTO answer = new UserQuizAnswerDTO();
        answer.setAnswer(1);
        String token = jwtTokenProvider.generateUserToken("testUser");

        ResponseEntity<Map<String, UserQuizStatus>> response = userService.submitQuiz(answer, "Bearer " + token);
        assertThat(response.getBody().get("status")).isEqualTo(UserQuizStatus.RIGHT);

        // Verify save method was called once
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testSubmitQuizWrongAnswer() {
        UserQuizAnswerDTO answer = new UserQuizAnswerDTO();
        answer.setAnswer(2);
        String token = jwtTokenProvider.generateUserToken("testUser");

        ResponseEntity<Map<String, UserQuizStatus>> response = userService.submitQuiz(answer, "Bearer " + token);
        assertThat(response.getBody().get("status")).isEqualTo(UserQuizStatus.WRONG);
    }

    @Test
    public void testSubmitQuizAlreadyParticipated() {
        UserQuizAnswerDTO answer = new UserQuizAnswerDTO();
        answer.setAnswer(1);
        String token = jwtTokenProvider.generateUserToken("testUser");

        userService.submitQuiz(answer, "Bearer " + token);
        ResponseEntity<Map<String, UserQuizStatus>> response = userService.submitQuiz(answer, "Bearer " + token);
        assertThat(response.getBody().get("status")).isEqualTo(UserQuizStatus.PARTICIPATED);
    }

    @Test
    public void testDummyToken() {
        when(jwtTokenProvider.generateUserToken(anyString())).thenReturn("dummyToken");
        ResponseEntity<Map<String, String>> response = userService.dummyToken();
        assertThat(response.getBody()).containsKey("accessToken");
    }

    @Test
    public void testKakaoLoginNewUser() {
        UserKakaoInfoDTO userKakaoInfoDTO = new UserKakaoInfoDTO();
        userKakaoInfoDTO.setEmail("kakaoUser@example.com");
        userKakaoInfoDTO.setNickname("KakaoUser");
    }
}



