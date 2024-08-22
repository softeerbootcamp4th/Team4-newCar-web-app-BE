package newCar.event_page.service;

import newCar.event_page.model.dto.user.*;
import newCar.event_page.model.entity.event.EventCommon;
import newCar.event_page.model.entity.event.EventId;
import newCar.event_page.model.entity.event.EventUser;
import newCar.event_page.model.entity.event.quiz.Quiz;
import newCar.event_page.model.entity.event.quiz.QuizWinner;
import newCar.event_page.model.entity.event.racing.PersonalityTest;
import newCar.event_page.model.enums.Team;
import newCar.event_page.model.enums.UserQuizStatus;
import newCar.event_page.repository.jpa.*;
import newCar.event_page.repository.jpa.quiz.QuizRepository;
import newCar.event_page.repository.jpa.quiz.QuizWinnerRepository;
import newCar.event_page.repository.jpa.racing.PersonalityTestRepository;
import newCar.event_page.jwt.JwtTokenProvider;
import newCar.event_page.config.JwtConfig;
import newCar.event_page.service.;
import newCar.event_page.model.entity.event.Event;
import newCar.event_page.model.entity.UserLight;
import newCar.event_page.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserLightRepository userLightRepository;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private PersonalityTestRepository personalityTestRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private QuizRepository quizRepository;
    @Mock
    private EventCommonRepository eventCommonRepository;
    @Mock
    private EventUserRepository eventUserRepository;
    @Mock
    private QuizWinnerRepository quizWinnerRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    @Mock
    private JwtConfig jwtConfig;

    private long key = 123456L;


    @BeforeEach
    void setUp() {
       // when(jwtConfig.getExpiration()).thenReturn(key);
    }
    @Test
    @DisplayName("성격유형검사 리스트 불러오기 _ 성공")
    void getPersonalityTestList_Success() {
        //given
        List<PersonalityTest> personalityTests = List.of(new PersonalityTest(), new PersonalityTest());
        when(personalityTestRepository.findAllByOrderByIdAsc()).thenReturn(personalityTests);
        UserPersonalityTestDTO userPersonalityTestDTO = mock(UserPersonalityTestDTO.class);

        //try-resource 문으로 메모리를 많이 잡아먹는 MockedStatic을  실행이 끝난후 바로 해제시켜준다
        try (MockedStatic<UserPersonalityTestDTO> mockedStatic = mockStatic(UserPersonalityTestDTO.class)) {
            mockedStatic.when(() -> UserPersonalityTestDTO.toDTO(any(PersonalityTest.class)))
                    .thenAnswer(invocation -> userPersonalityTestDTO);

            //when
            ResponseEntity<List<UserPersonalityTestDTO>> response = userService.getPersonalityTestList();

            //then
            assertThat(response).isNotNull();
            assertThat(response.getBody().size()).isEqualTo(2);
        }
    }

    @Test
    @DisplayName("오늘의 퀴즈 불러오기 _ 성공")
    void getQuiz_Success() {
        //given
        Event event = new Event();
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));

        ValueOperations valueOperations = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        Quiz quiz = new Quiz();
        quiz.setId(1L);
        quiz.setPostDate(LocalDate.now(ZoneId.of("Asia/Seoul")));
        when(quizRepository.findByPostDate(any(LocalDate.class))).thenReturn(Optional.of(quiz));
        when(redisTemplate.opsForValue().decrement("ticket_1")).thenReturn(1L);


        //when
        ResponseEntity<UserQuizDTO> response = userService.getQuiz(1L);

        //then
        assertNotNull(response);
        assertEquals(quiz.getId(), response.getBody().getId());
    }

    @Test
    @DisplayName("오늘의 퀴즈 불러오기 _ 실패")
    void getQuiz_Success() {
        //given
        Event event = new Event();
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));

        ValueOperations valueOperations = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        Quiz quiz = new Quiz();
        quiz.setId(1L);
        quiz.setPostDate(LocalDate.now(ZoneId.of("Asia/Seoul")));
        when(quizRepository.findByPostDate(any(LocalDate.class))).thenReturn(Optional.of(quiz));
        when(redisTemplate.opsForValue().decrement("ticket_1")).thenReturn(1L);


        //when & then
        assertThatThrownBy(() -> userService.getQuiz(EventId.Quiz.getValue())
                .isInstanceOf(IndexOutOfBoundsException.class);


    }

    @Test
    void getEventTime_shouldReturnEventTime() {
        EventCommon eventCommon = new EventCommon();
        when(eventCommonRepository.findById(1L)).thenReturn(Optional.of(eventCommon));
        UserEventTimeDTO userEventTimeDTO = mock(UserEventTimeDTO.class);

        try (MockedStatic<UserEventTimeDTO> mockedStatic = mockStatic(UserEventTimeDTO.class)) {
            mockedStatic.when(() -> UserEventTimeDTO.toDTO(any(EventCommon.class)))
                    .thenAnswer(invocation -> userEventTimeDTO);

            ResponseEntity<UserEventTimeDTO> response = userService.getEventTime();

            assertNotNull(response);
        }
    }

    @Test
    void login_shouldReturnAccessToken() {
        UserLight userLight = new UserLight();
        userLight.setUserId("testUser");
        userLight.setPassword("testPassword");

        when(userLightRepository.findById(1L)).thenReturn(Optional.of(userLight));
        when(jwtTokenProvider.generateUserToken(anyString())).thenReturn("testAccessToken");

        UserLightDTO userLightDTO = new UserLightDTO();
        userLightDTO.setUserId("testUser");
        userLightDTO.setPassword("testPassword");

        ResponseEntity<Map<String, String>> response = userService.login(userLightDTO);

        assertNotNull(response);
        assertEquals("testAccessToken", response.getBody().get("accessToken"));
    }

    @Test
    void submitPersonalityTest_shouldAssignTeamAndReturnUrl() {
        User user = new User();
        user.setId(1L);

        when(jwtTokenProvider.getUserId(anyString())).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(jwtTokenProvider.generateTokenWithTeam(any(Team.class), anyString())).thenReturn("token");

        List<UserPersonalityAnswerDTO> answers = List.of(new UserPersonalityAnswerDTO(), new UserPersonalityAnswerDTO());

        ResponseEntity<UserPersonalityUrlDTO> response = userService.submitPersonalityTest(answers, "authHeader");

        assertNotNull(response);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void submitQuiz_shouldReturnCorrectQuizStatus() {
        User user = new User();
        user.setId(1L);

        when(jwtTokenProvider.getUserId(anyString())).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Quiz quiz = new Quiz();
        quiz.setId(1L);
        quiz.setCorrectAnswer(1);
        when(quizRepository.findByPostDate(any(LocalDate.class))).thenReturn(Optional.of(quiz));

        EventUser eventUser = new EventUser();
        when(eventUserRepository.findByUserIdAndEventId(anyLong(), anyLong())).thenReturn(eventUser);

        UserQuizAnswerDTO answerDTO = new UserQuizAnswerDTO();

        Map<String, UserQuizStatus> response = userService.submitQuiz(answerDTO, "authHeader").getBody();

        assertNotNull(response);
        verify(quizWinnerRepository, times(1)).save(any(QuizWinner.class));
    }

    @Test
    void plusClickNumber_shouldIncreaseClickNumber() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(null);

        User user = new User();
        user.setId(1L);
        user.setClickNumber(0);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        ResponseEntity<Void> response = userService.plusClickNumber("url", request);

        assertNotNull(response);
        verify(userRepository, times(1)).save(user);
        assertEquals(1, user.getClickNumber());
    }

    @Test
    void getUserInfo_shouldReturnUserInfo() {
        //given
        User user = new User();
        user.setId(1L);
        UserInfoDTO userInfoDTO = mock(UserInfoDTO.class);

        when(jwtTokenProvider.getUserId(anyString())).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        try (MockedStatic<UserInfoDTO> mockedStatic = mockStatic(UserInfoDTO.class)) {
            mockedStatic.when(() -> UserInfoDTO.toDTO(any(User.class)))
                    .thenAnswer(invocation -> userInfoDTO);

            //when
            ResponseEntity<UserInfoDTO> response = userService.getUserInfo("authHeader");

            //then
            assertNotNull(response);
        }
    }

    @Test
    void kakaoLogin_shouldReturnAccessToken() {
        //given
        User user = new User();
        user.setUserName("testUser");

        when(userRepository.findByUserName(anyString())).thenReturn(Optional.of(user));
        when(jwtTokenProvider.generateUserToken(anyString())).thenReturn("testAccessToken");

        UserKakaoInfoDTO kakaoInfoDTO = new UserKakaoInfoDTO();
        kakaoInfoDTO.setEmail("testUser");
        kakaoInfoDTO.setNickname("testNick");

        //when
        Map<String, String> response = userService.kakaoLogin(kakaoInfoDTO);

        //then
        assertNotNull(response);
        assertEquals("testAccessToken", response.get("accessToken"));
    }

    @Test
    void dummyToken_shouldReturnDummyAccessToken() {

        //given
        when(jwtTokenProvider.generateUserToken(anyString())).thenReturn("dummyToken");

        //when
        ResponseEntity<Map<String, String>> response = userService.dummyToken();

        //then
        assertNotNull(response);
        assertEquals("dummyToken", response.getBody().get("accessToken"));
    }
}