package newCar.event_page.service;

import jakarta.servlet.http.HttpServletRequest;
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
import newCar.event_page.model.entity.event.quiz.QuizWinner;
import newCar.event_page.model.entity.event.racing.PersonalityTest;
import newCar.event_page.model.entity.event.racing.RacingWinner;
import newCar.event_page.model.enums.Team;
import newCar.event_page.model.enums.UserQuizStatus;
import newCar.event_page.repository.jpa.*;
import newCar.event_page.repository.jpa.quiz.QuizRepository;
import newCar.event_page.repository.jpa.quiz.QuizWinnerRepository;
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
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @Mock
    private EventUserRepository eventUserRepository;

    @Mock
    private QuizWinnerRepository quizWinnerRepository;


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

        //when & then
        assertThatThrownBy(() -> userService.getQuiz(EventId.Quiz.getValue()))
                .isInstanceOf(NoSuchElementException.class);
        verify(eventRepository,times(1)).findById(any(Long.class));
    }

    @Test
    @DisplayName("오늘의 퀴즈 가져오기 실패_ 아직 시작 안함")
    public void testGetQuizNotStartedYet() {
        //gvien
        Event event = mock(Event.class);
        when(eventRepository.findById(any(Long.class))).thenReturn(Optional.of(event));
        Quiz quiz = mock(Quiz.class);
        when(quizRepository.findByPostDate(any(LocalDate.class))).thenReturn(Optional.of(quiz))
        ;
        //when & then
        assertThatThrownBy(() -> userService.getQuiz(EventId.Quiz.getValue()))
                .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    @DisplayName("선착순 순위권에 못 들었을때")
    public void testGetQuizFinished() {
        //gvien
        Event event = mock(Event.class);
        when(eventRepository.findById(any(Long.class))).thenReturn(Optional.of(event));
        Quiz quiz = mock(Quiz.class);
        when(quizRepository.findByPostDate(any(LocalDate.class))).thenReturn(Optional.of(quiz));
        List<Boolean> availableArray = Arrays.asList(false, true, true);


        userService.setQuizAvailableArray(new ArrayList<>(availableArray));
        assertThatThrownBy(()->userService.getQuiz(1L))
                .isInstanceOf(FCFSFinishedException.class);
    }

    @Test
    @DisplayName("라이트한 유저 로그인 성공")
    public void testLoginSuccess() {
        UserLightDTO userLightDTO = new UserLightDTO();
        userLightDTO.setUserId("testUser");
        userLightDTO.setPassword("password");

        UserLight userLight = new UserLight();
        userLight.setUserId("testUser");
        userLight.setPassword("password");

        when(userLightRepository.findById(any(Long.class))).thenReturn(Optional.of(userLight));
        when(jwtTokenProvider.generateUserToken(anyString())).thenReturn("dummyToken");

        ResponseEntity<Map<String, String>> response = userService.login(userLightDTO);

        //then
        assertThat(response.getBody()).containsKey("accessToken");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("light유저 로그인 실패")
    public void testLoginFail() {

        //given
        UserLightDTO userLightDTO = new UserLightDTO();
        userLightDTO.setUserId("testUser");
        userLightDTO.setPassword("wrongPassword");

        UserLight user = new UserLight();
        user.setUserId("testUser");
        user.setPassword("1234");

        when(userLightRepository.findById(any(Long.class))).thenReturn(Optional.of(user));

        //when & then
        assertThatThrownBy(()->userService.login(userLightDTO))
                .isInstanceOf(UserLoginFailException.class);
    }

    @Test
    @DisplayName("getEventTime()_성공")
    public void getEventTime_Success(){
        //given
        EventCommon eventCommon = mock(EventCommon.class);
        when(eventCommonRepository.findById(any(Long.class))).thenReturn(Optional.of(eventCommon));

        //when
        ResponseEntity<UserEventTimeDTO> response = userService.getEventTime();

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("성격유형검사 제출 _성공")
    public void testSubmitPersonalityTest_Success() {

        //given
        List<UserPersonalityAnswerDTO> answers = new ArrayList<>();
        UserPersonalityAnswerDTO answer1 = new UserPersonalityAnswerDTO();
        answer1.setId(1L);
        answer1.setAnswer(1);
        UserPersonalityAnswerDTO answer2 = new UserPersonalityAnswerDTO();
        answer2.setId(2L);
        answer2.setAnswer(1);

        PersonalityTest personalityTest = mock(PersonalityTest.class);
        when(personalityTestRepository.findById(any(Long.class))).thenReturn(Optional.of(personalityTest));

        answers.add(answer1);
        answers.add(answer2);
        String token = jwtTokenProvider.generateUserToken("testUser");

        //when
        ResponseEntity<UserPersonalityUrlDTO> response = userService.submitPersonalityTest(answers, "Bearer " + token);

        //then
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
    @DisplayName("이미 선착순 이벤트에 참가한 참가자 테스트")
    public void testSubmitQuiz_Participated() {
        UserQuizAnswerDTO answer = new UserQuizAnswerDTO();
        answer.setAnswer(1);
        String token = jwtTokenProvider.generateUserToken("testUser");

        User user = mock(User.class);
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        EventUser eventUser = mock(EventUser.class);
        when(eventUserRepository.findByUserIdAndEventId(any(Long.class),any(Long.class))).thenReturn(eventUser);
        Quiz quiz = mock(Quiz.class);
        when(quizRepository.findByPostDate(any(LocalDate.class))).thenReturn(Optional.of(quiz));
        QuizWinner quizWinner = mock(QuizWinner.class);
        when(quizWinnerRepository.findByQuiz_IdAndEventUser_Id(any(Long.class),any(Long.class))).thenReturn(Optional.of(quizWinner));

        ResponseEntity<Map<String, UserQuizStatus>> response = userService.submitQuiz(answer, token);
        assertThat(response.getBody().get("status")).isEqualTo(UserQuizStatus.PARTICIPATED);

    }

    @Test
    @DisplayName("잘못된 정답 제출했을시 ")
    public void testSubmitQuiz_Wrong() {
        UserQuizAnswerDTO answer = new UserQuizAnswerDTO();
        answer.setAnswer(1);
        String token = jwtTokenProvider.generateUserToken("testUser");

        User user = mock(User.class);
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        EventUser eventUser = mock(EventUser.class);
        when(eventUserRepository.findByUserIdAndEventId(any(Long.class),any(Long.class))).thenReturn(eventUser);
        Quiz quiz = mock(Quiz.class);
        when(quizRepository.findByPostDate(any(LocalDate.class))).thenReturn(Optional.of(quiz));
        when(quizWinnerRepository.findByQuiz_IdAndEventUser_Id(any(Long.class),any(Long.class))).thenReturn(Optional.empty());

        ResponseEntity<Map<String, UserQuizStatus>> response = userService.submitQuiz(answer, token);
        assertThat(response.getBody().get("status")).isEqualTo(UserQuizStatus.WRONG);

    }

    @Test
    @DisplayName("선착순 이벤트 마감 되었을 때 테스트")
    public void testSubmitQuiz_End() {
        UserQuizAnswerDTO answer = new UserQuizAnswerDTO();
        answer.setAnswer(1);
        String token = jwtTokenProvider.generateUserToken("testUser");

        User user = mock(User.class);
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        EventUser eventUser = mock(EventUser.class);
        when(eventUserRepository.findByUserIdAndEventId(any(Long.class),any(Long.class))).thenReturn(eventUser);
        Quiz quiz = new Quiz();
        quiz.setCorrectAnswer(1);
        quiz.setId(1L);
        when(quizRepository.findByPostDate(any(LocalDate.class))).thenReturn(Optional.of(quiz));
        when(quizWinnerRepository.findByQuiz_IdAndEventUser_Id(any(Long.class),any(Long.class))).thenReturn(Optional.empty());

        List<Boolean> availableArray = Arrays.asList(false, true, true);
        userService.setQuizAvailableArray(new ArrayList<>(availableArray));

        ResponseEntity<Map<String, UserQuizStatus>> response = userService.submitQuiz(answer, token);
        assertThat(response.getBody().get("status")).isEqualTo(UserQuizStatus.END);
    }

    @Test
    @DisplayName("선착순 이벤트 해당 유저가 티켓 수 0에 접근할 때")
    public void testSubmitQuiz_End_Zero() {
        UserQuizAnswerDTO answer = new UserQuizAnswerDTO();
        answer.setAnswer(1);
        String token = jwtTokenProvider.generateUserToken("testUser");

        User user = mock(User.class);
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        EventUser eventUser = mock(EventUser.class);
        when(eventUserRepository.findByUserIdAndEventId(any(Long.class),any(Long.class))).thenReturn(eventUser);
        Quiz quiz = new Quiz();
        quiz.setCorrectAnswer(1);
        quiz.setId(1L);
        when(quizRepository.findByPostDate(any(LocalDate.class))).thenReturn(Optional.of(quiz));
        when(quizWinnerRepository.findByQuiz_IdAndEventUser_Id(any(Long.class),any(Long.class))).thenReturn(Optional.empty());

        List<Boolean> availableArray = Arrays.asList(true, true, true);
        userService.setQuizAvailableArray(new ArrayList<>(availableArray));

        ValueOperations valueOperations = mock(ValueOperations.class);  // ValueOperations mock 생성
        // RedisTemplate의 opsForValue() 메서드가 ValueOperations 객체를 반환하도록 설정
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(redisTemplate.opsForValue().decrement(any(String.class))).thenReturn(-1L);

        ResponseEntity<Map<String, UserQuizStatus>> response = userService.submitQuiz(answer, token);
        assertThat(response.getBody().get("status")).isEqualTo(UserQuizStatus.END);
    }

    @Test
    @DisplayName("선착순 퀴즈 풀기 성공")
    public void testSubmitQuiz_Right() {
        UserQuizAnswerDTO answer = new UserQuizAnswerDTO();
        answer.setAnswer(1);
        String token = jwtTokenProvider.generateUserToken("testUser");

        User user = mock(User.class);
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        EventUser eventUser = mock(EventUser.class);
        when(eventUserRepository.findByUserIdAndEventId(any(Long.class),any(Long.class))).thenReturn(eventUser);
        Quiz quiz = new Quiz();
        quiz.setCorrectAnswer(1);
        quiz.setId(1L);
        when(quizRepository.findByPostDate(any(LocalDate.class))).thenReturn(Optional.of(quiz));
        when(quizWinnerRepository.findByQuiz_IdAndEventUser_Id(any(Long.class),any(Long.class))).thenReturn(Optional.empty());

        List<Boolean> availableArray = Arrays.asList(true, true, true);
        userService.setQuizAvailableArray(new ArrayList<>(availableArray));

        ValueOperations valueOperations = mock(ValueOperations.class);  // ValueOperations mock 생성
        // RedisTemplate의 opsForValue() 메서드가 ValueOperations 객체를 반환하도록 설정
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        //then
        ResponseEntity<Map<String, UserQuizStatus>> response = userService.submitQuiz(answer, token);
        assertThat(response.getBody().get("status")).isEqualTo(UserQuizStatus.RIGHT);
    }

    @Test
    @DisplayName("공유링크 클릭수 Get_성공")
    public void getClickNumber_Success(){
        //given
        User user = mock(User.class);
        when(jwtTokenProvider.getUserId(any(String.class))).thenReturn(1L);
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));


        //when
        ResponseEntity<UserClickNumberDTO> response = userService.getClickNumber("abc");

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

    }

    @Test
    @DisplayName("plusClikcNumber_성공")
    public void plusClickNumber_Success(){

        //given
        User user = mock(User.class);
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        HttpServletRequest request = mock(HttpServletRequest.class);
        //when
        ResponseEntity<Void> response = userService.plusClickNumber("df", request);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
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



