package newCar.event_page.service;

import newCar.event_page.exception.*;
import newCar.event_page.exception.FCFS.FCFSNotYetConductedException;
import newCar.event_page.jwt.JwtTokenProvider;
import newCar.event_page.model.dto.admin.*;
import newCar.event_page.model.entity.Administrator;
import newCar.event_page.model.entity.TeamScore;
import newCar.event_page.model.entity.User;
import newCar.event_page.model.entity.event.Event;
import newCar.event_page.model.entity.event.EventCommon;
import newCar.event_page.model.entity.event.EventId;
import newCar.event_page.model.entity.event.EventUser;
import newCar.event_page.model.entity.event.quiz.Quiz;
import newCar.event_page.model.entity.event.quiz.QuizEvent;
import newCar.event_page.model.entity.event.quiz.QuizWinner;
import newCar.event_page.model.entity.event.racing.PersonalityTest;
import newCar.event_page.model.entity.event.racing.RacingWinner;
import newCar.event_page.model.enums.Team;
import newCar.event_page.repository.jpa.*;
import newCar.event_page.repository.jpa.quiz.*;
import newCar.event_page.repository.jpa.racing.*;

import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceImplTest {

    @InjectMocks
    private AdminServiceImpl adminServiceImpl;

    @Mock
    private EventCommonRepository eventCommonRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private EventUserRepository eventUserRepository;

    @Mock
    private QuizRepository quizRepository;
    @Mock
    private QuizEventRepository quizEventRepository;
    @Mock
    private QuizWinnerRepository quizWinnerRepository;

    @Mock
    private RacingWinnerRepository racingWinnerRepository;
    @Mock
    private RacingEventRepository racingEventRepository;
    @Mock
    private PersonalityTestRepository personalityTestRepository;

    @Mock
    private AdministratorRepository administratorRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private UserServiceImpl userServiceImpl;

    private static MockedStatic<AdminQuizWinnersDTO> mockedAdminQuizWinnersDTO;

    @BeforeAll
    public static void beforeClass(){
        mockedAdminQuizWinnersDTO = mockStatic(AdminQuizWinnersDTO.class);
    }

    @AfterAll
    public static void afterClass(){
        mockedAdminQuizWinnersDTO.close();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("공통 이벤트 정보 가져오기 - 성공")
    void getCommonEventDetails_Success() {
        // given
        EventCommon eventCommon = mock(EventCommon.class);
        when(eventCommonRepository.findById(1L)).thenReturn(Optional.of(eventCommon));

        // when
        ResponseEntity<AdminEventCommonDTO> response = adminServiceImpl.getCommonEventDetails();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(AdminEventCommonDTO.toDTO(eventCommon));
        verify(eventCommonRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("공통 이벤트 정보 가져오기 - 실패")
    void getCommonEventDetails_NoSuchElement() {
        // given
        when(eventCommonRepository.findById(1L)).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> adminServiceImpl.getCommonEventDetails())
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("공통 이벤트 정보 업데이트 - 성공")
    void updateCommonEvent_Success() {
        // given
        AdminEventCommonDTO eventCommonDTO = mock(AdminEventCommonDTO.class);
        EventCommon eventCommon = mock(EventCommon.class);
        QuizEvent quizEvent = mock(QuizEvent.class);

        // 설정된 값
        when(eventCommonDTO.getEventName()).thenReturn("Updated Event");
        when(eventCommonDTO.getManagerName()).thenReturn("Updated Manager");
        when(eventCommonDTO.getStartTime()).thenReturn(LocalDateTime.of(2024, 1, 31, 18, 30));
        when(eventCommonDTO.getEndTime()).thenReturn(LocalDateTime.of(2024, 2, 28, 18, 30));

        when(eventCommonRepository.findById(1L)).thenReturn(Optional.of(eventCommon));
        when(quizRepository.findAllByOrderByIdAsc()).thenReturn(Collections.emptyList());
        when(quizEventRepository.findById(any(Long.class))).thenReturn(Optional.of(quizEvent));

        // when
        ResponseEntity<AdminEventCommonDTO> response = adminServiceImpl.updateCommonEventDetails(eventCommonDTO);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(eventCommonRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("공통 이벤트 정보 업데이트 - 실패")
    void updateCommonEvent_NoSuchElement() {
        // given
        when(eventCommonRepository.findById(1L)).thenReturn(Optional.empty());

        // when, then
        AdminEventCommonDTO eventCommonDTO = mock(AdminEventCommonDTO.class);
        assertThatThrownBy(() -> adminServiceImpl.updateCommonEventDetails(eventCommonDTO))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("퀴즈 리스트 가져오기 - 성공")
    void getQuizList_Success() {
        // given
        when(eventRepository.findById(any(Long.class))).thenReturn(Optional.of(mock(Event.class)));
        when(quizRepository.findAllByOrderByPostDateAsc()).thenReturn(Collections.emptyList());

        // when
        ResponseEntity<List<AdminQuizDTO>> response = adminServiceImpl.getQuizList(1L);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("퀴즈 업데이트 - 성공")
    void updateQuiz_Success() {
        // given
        Quiz quiz = new Quiz();
        quiz.setId(1L);
        AdminQuizDTO adminQuizDTO = mock(AdminQuizDTO.class);
        ValueOperations valueOperations = mock(ValueOperations.class);  // ValueOperations mock 생성

        // AdminQuizDTO의 mock 설정
        when(adminQuizDTO.getId()).thenReturn(1L);
        when(adminQuizDTO.getWinnerCount()).thenReturn(50);
        when(adminQuizDTO.getQuestion()).thenReturn("Updated Question");
        when(adminQuizDTO.getChoices()).thenReturn(Arrays.asList(
                AdminQuizDTO.Choice.builder().num(0).text("Updated Choice 1").build(),
                AdminQuizDTO.Choice.builder().num(1).text("Updated Choice 2").build(),
                AdminQuizDTO.Choice.builder().num(2).text("Updated Choice 3").build(),
                AdminQuizDTO.Choice.builder().num(3).text("Updated Choice 4").build()
        ));
        when(adminQuizDTO.getCorrectAnswer()).thenReturn(2);
        when(adminQuizDTO.getPostDate()).thenReturn(null);

        when(quizRepository.findById(any(Long.class))).thenReturn(Optional.of(quiz));

        // RedisTemplate의 opsForValue() 메서드가 ValueOperations 객체를 반환하도록 설정
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);


        // when
        ResponseEntity<AdminQuizDTO> response = adminServiceImpl.updateQuiz(adminQuizDTO);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(quiz.getWinnerCount()).isEqualTo(adminQuizDTO.getWinnerCount());
        // Redis 저장값 검증
        verify(valueOperations).set("ticket_1", 50);
    }

    @Test
    @DisplayName("퀴즈 업데이트 - 실패")
    void updateQuiz_NoSuchElement() {
        // given
        when(quizRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // when, then
        AdminQuizDTO adminQuizDTO = mock(AdminQuizDTO.class);
        assertThatThrownBy(() -> adminServiceImpl.updateQuiz(adminQuizDTO))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("퀴즈 업데이트 - 게시글 날짜 수정은 불가능 합니다")
    void updateQuiz_UnmodifiableFieldException(){
        Long quizId = 1L;
        AdminQuizDTO adminQuizDTO = AdminQuizDTO.builder()
                .id(quizId)
                .postDate(LocalDate.now()) // postDate를 설정하여 예외 발생 조건 충족
                .winnerCount(100)
                .question("Sample Question")
                .choices(Arrays.asList(
                        AdminQuizDTO.Choice.builder().num(0).text("Choice 1").build(),
                        AdminQuizDTO.Choice.builder().num(1).text("Choice 2").build(),
                        AdminQuizDTO.Choice.builder().num(2).text("Choice 3").build(),
                        AdminQuizDTO.Choice.builder().num(3).text("Choice 4").build()
                ))
                .correctAnswer(1)
                .build();

        when(quizRepository.findById(quizId)).thenReturn(Optional.of(mock(Quiz.class)));

        assertThatThrownBy(() -> adminServiceImpl.updateQuiz(adminQuizDTO))
                .isInstanceOf(UnmodifiableFieldException.class);

    }

    @Test
    @DisplayName("레이싱 당첨자 추첨 - 실패")
    void drawRacingWinners_ExcessiveWinners() {
        // given
        List<AdminWinnerSettingDTO> winnerSettingDTOList = List.of(new AdminWinnerSettingDTO(1, 1));
        when(eventUserRepository.findByEventId(any(Long.class))).thenReturn(Collections.emptyList());

        // when, then
        assertThatThrownBy(() -> adminServiceImpl.drawRacingWinners(winnerSettingDTOList, 1L))
                .isInstanceOf(ExcessiveWinnersRequestedException.class);
    }

    @Test
    @DisplayName("레이싱 당첨자 리스트 가져오기 - 성공")
    void getRacingWinnerList_Success() {
        // given
        RacingWinner mockWinner = mock(RacingWinner.class);
        EventUser mockEventUser = mock(EventUser.class);
        User mockUser = mock(User.class);

        // 각 객체의 동작을 설정해줍니다.
        when(mockWinner.getRank()).thenReturn(1);
        when(mockWinner.getEventUser()).thenReturn(mockEventUser);
        when(mockEventUser.getUser()).thenReturn(mockUser);
        when(mockUser.getNickName()).thenReturn("testNickName");
        when(mockUser.getUserName()).thenReturn("010-1234-5678");
        when(mockUser.getClickNumber()).thenReturn(10);
        when(mockUser.getTeam()).thenReturn(Team.PET);  // 예시로 팀이 RED라고 설정

        // 리스트로 반환
        List<RacingWinner> winnerList = List.of(mockWinner);
        when(racingWinnerRepository.findByEventId(any(Long.class))).thenReturn(winnerList);

        // when
        ResponseEntity<List<AdminRacingWinnersDTO>> response = adminServiceImpl.getRacingWinnerList(1L);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getName()).isEqualTo("testNickName");

    }

    @Test
    @DisplayName("레이싱 당첨자 리스트 가져오기 - 실패")
    void getRacingWinnerList_NotConducted() {
        // given
        when(racingWinnerRepository.findByEventId(any(Long.class))).thenReturn(Collections.emptyList());

        // when, then
        assertThatThrownBy(() -> adminServiceImpl.getRacingWinnerList(1L))
                .isInstanceOf(DrawNotYetConductedException.class);
    }
    @Test
    @DisplayName("선착순 퀴즈 당첨자 불러오기 - 당첨자 추첨이 아직 이뤄지지 않았을때")
    public void testGetQuizWinnerList_WhenNoWinners_ThrowsFCFSNotYetConductedException() {
        when(quizWinnerRepository.findAllByOrderByQuiz_Id()).thenReturn(Collections.emptyList());

        // When & Then
        assertThatThrownBy(() -> adminServiceImpl.getQuizWinnerList(EventId.Quiz.getValue()))
                .isInstanceOf(FCFSNotYetConductedException.class);

        // quizWinnerRepository의 메서드가 호출되었는지 확인
        verify(quizWinnerRepository, times(1)).findAllByOrderByQuiz_Id();
    }

    @Test
    @DisplayName("선착순 퀴즈 당첨자 불러오기 - 성공")
    public void testGetQuizWinnerList_WhenWinnersExist_ReturnsWinnerList() {
        // Given
        Long quizEventId = 1L;

        QuizWinner quizWinner = mock(QuizWinner.class);
        List<QuizWinner> quizWinnerList = List.of(quizWinner);

        when(quizWinnerRepository.findAllByOrderByQuiz_Id()).thenReturn(quizWinnerList);

        AdminQuizWinnersDTO expectedDTO = mock(AdminQuizWinnersDTO.class);

        when(AdminQuizWinnersDTO.toDTO(quizWinner)).thenReturn(expectedDTO);

        // When
        ResponseEntity<List<AdminQuizWinnersDTO>> response = adminServiceImpl.getQuizWinnerList(quizEventId);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // quizWinnerRepository의 메서드가 호출되었는지 확인
        verify(quizWinnerRepository, times(1)).findAllByOrderByQuiz_Id();
    }

    @Test
    @DisplayName("유형 검사 리스트 가져오기 - 성공")
    void getPersonalityTestList_Success() {
        // given
        when(personalityTestRepository.findAllByOrderByIdAsc()).thenReturn(Collections.emptyList());

        // when
        ResponseEntity<List<AdminPersonalityTestDTO>> response = adminServiceImpl.getPersonalityTestList();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("유형 검사 업데이트 - 성공")
    void updatePersonalityTest_Success() {
        // given
        PersonalityTest personalityTest = mock(PersonalityTest.class);
        AdminPersonalityTestDTO adminPersonalityTestDTO = mock(AdminPersonalityTestDTO.class);

        // PersonalityTest의 반환 값을 설정
        when(personalityTest.getId()).thenReturn(1L);
        when(personalityTest.getQuestion()).thenReturn("What is your favorite hobby?");
        when(personalityTest.getChoice1()).thenReturn("choice1");
        when(personalityTest.getChoice2()).thenReturn("choice2");
        when(personalityTest.getChoice1Scores()).thenReturn(new TeamScore(1,1,1,1));
        when(personalityTest.getChoice2Scores()).thenReturn(new TeamScore(2,2,2,2));

        // personalityTestDTO에 대한 동작 설정
        when(adminPersonalityTestDTO.getId()).thenReturn(1L);

        // PersonalityTestRepository mock
        when(personalityTestRepository.findById(any(Long.class))).thenReturn(Optional.of(personalityTest));

        // when
        ResponseEntity<AdminPersonalityTestDTO> response = adminServiceImpl.updatePersonalityTest(adminPersonalityTestDTO);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1L);
        assertThat(response.getBody().getQuestion()).isEqualTo("What is your favorite hobby?");
        assertThat(response.getBody().getChoices()).hasSize(2);
    }

    @Test
    @DisplayName("유형 검사 업데이트 - 실패")
    void updatePersonalityTest_NoSuchElement() {
        // given
        when(personalityTestRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // when
        AdminPersonalityTestDTO personalityTestDTO = mock(AdminPersonalityTestDTO.class);
        personalityTestDTO.setId(1L);

        //then
        assertThatThrownBy(() -> adminServiceImpl.updatePersonalityTest(personalityTestDTO))
                .isInstanceOf(NoSuchElementException.class);
    }


    @Test
    @DisplayName("로그인 - 성공")
    void login_Success() {
        // given
        Administrator administrator = mock(Administrator.class);
        when(administrator.getAdminId()).thenReturn("admin");
        when(administrator.getPassword()).thenReturn("1234");
        when(administratorRepository.findById(1L)).thenReturn(Optional.of(administrator));
        when(jwtTokenProvider.generateAdminToken()).thenReturn("token");

        AdminLoginDTO adminLoginDTO = new AdminLoginDTO();
        adminLoginDTO.setAdminId("admin");
        adminLoginDTO.setPassword("1234");

        // when
        ResponseEntity<Map<String, String>> response = adminServiceImpl.login(adminLoginDTO);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsEntry("accessToken", "token");
    }

    @Test
    @DisplayName("로그인 - 아이디 달라서 실패")
    void login_Fail_Id() {
        // given
        Administrator administrator = mock(Administrator.class);
        when(administrator.getAdminId()).thenReturn("admin");
        when(administrator.getPassword()).thenReturn("1234");
        when(administratorRepository.findById(1L)).thenReturn(Optional.of(administrator));

        AdminLoginDTO adminLoginDTO = new AdminLoginDTO();
        adminLoginDTO.setAdminId("user");
        adminLoginDTO.setPassword("1234");

        // when, then
        assertThatThrownBy(() -> adminServiceImpl.login(adminLoginDTO))
                .isInstanceOf(AdminLoginFailException.class);
        //잘못된 admin 아이디와 비밀번호가 들어왔을때 Exception 터져야 한다

    }

    @Test
    @DisplayName("로그인 - 비밀번호 달라서 실패")
    void login_Fail_Password() {
        // given
        Administrator administrator = mock(Administrator.class);
        when(administrator.getAdminId()).thenReturn("admin");
        when(administrator.getPassword()).thenReturn("1234");
        when(administratorRepository.findById(1L)).thenReturn(Optional.of(administrator));

        AdminLoginDTO adminLoginDTO = new AdminLoginDTO();
        adminLoginDTO.setAdminId("admin");
        adminLoginDTO.setPassword("4321");

        // when, then
        assertThatThrownBy(() -> adminServiceImpl.login(adminLoginDTO))
                .isInstanceOf(AdminLoginFailException.class);
        //잘못된 admin 아이디와 비밀번호가 들어왔을때 Exception 터져야 한다

    }
}
