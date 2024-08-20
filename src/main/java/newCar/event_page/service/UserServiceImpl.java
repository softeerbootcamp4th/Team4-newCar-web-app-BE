package newCar.event_page.service;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import newCar.event_page.config.JwtConfig;
import newCar.event_page.exception.FCFS.FCFSFinishedException;
import newCar.event_page.exception.FCFS.FCFSNotStartedYet;
import newCar.event_page.exception.UserAlreadyHasTeamException;
import newCar.event_page.exception.UserLoginFailException;
import newCar.event_page.jwt.JwtTokenProvider;
import newCar.event_page.model.dto.user.*;
import newCar.event_page.model.entity.event.EventId;
import newCar.event_page.model.entity.event.EventUser;
import newCar.event_page.model.entity.event.quiz.QuizWinner;
import newCar.event_page.model.enums.LoginType;
import newCar.event_page.model.enums.Team;

import newCar.event_page.model.entity.TeamScore;
import newCar.event_page.model.entity.User;

import newCar.event_page.model.entity.UserLight;
import newCar.event_page.model.entity.event.Event;
import newCar.event_page.model.entity.event.EventCommon;
import newCar.event_page.model.entity.event.quiz.Quiz;
import newCar.event_page.model.entity.event.racing.PersonalityTest;
import newCar.event_page.model.enums.UserQuizStatus;
import newCar.event_page.repository.jpa.*;
import newCar.event_page.repository.jpa.quiz.QuizRepository;
import newCar.event_page.repository.jpa.quiz.QuizWinnerRepository;
import newCar.event_page.repository.jpa.racing.PersonalityTestRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;


@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserLightRepository userLightRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PersonalityTestRepository personalityTestRepository;
    private final EventRepository eventRepository;
    private final QuizRepository quizRepository;
    private final EventCommonRepository eventCommonRepository;

    private final EventUserRepository eventUserRepository;

    private final QuizWinnerRepository quizWinnerRepository;

    private final UserRepository userRepository;

    private ArrayList<Boolean> isQuizAvailable = new ArrayList<>();

    private final RedisTemplate<String,Object> redisTemplate;

    private final JwtConfig jwtConfig;

    private long key ;

    @PostConstruct
    private void Init(){
        EventCommon eventCommon = eventCommonRepository.findById(1L).get();
        long count = eventCommon.getDuration();
        for(int i = 0; i < count; i++){
            isQuizAvailable.add(true);
        }
        List<Quiz> quizList = quizRepository.findAllByOrderByIdAsc();
        for(int i = 0 ; i<count ;i++){
            Quiz quiz = quizList.get(i);
            redisTemplate.opsForValue().set("ticket_"+quiz.getId(), quiz.getWinnerCount());
        }
        key = jwtConfig.getExpiration();
    }//common업데이트, 퀴즈 업데이트 맞춰서

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<List<UserPersonalityTestDTO>> getPersonalityTestList() {
        return ResponseEntity.ok(personalityTestRepository.findAllByOrderByIdAsc()
                .stream()
                .map(UserPersonalityTestDTO::toDTO)
                .toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<UserQuizDTO> getQuiz(Long quizEventId ) {

        Event quizEvent = eventRepository.findById(quizEventId)
                .orElseThrow(() -> new NoSuchElementException("퀴즈 이벤트가 존재하지 않습니다."));

        //LocalDate 한국 날짜를 기준으로 오늘의 퀴즈를 받아온다
        Quiz todayQuiz = quizRepository.findByPostDate(LocalDate.now(ZoneId.of("Asia/Seoul")))
                .orElseThrow(() -> new NoSuchElementException("오늘 날짜에 해당하는 퀴즈 이벤트가 존재하지 않습니다."));

        if(isQuizAvailable.size()<=todayQuiz.getId().intValue()){
            throw new IndexOutOfBoundsException("이벤트 기간이 지났습니다");
        }

        if(!isQuizAvailable.get(todayQuiz.getId().intValue())){
            throw new FCFSFinishedException("선착순 퀴즈가 마감되었습니다");
        }//오늘 퀴즈가 마감되었다면

        if(LocalDateTime.now(ZoneId.of("Asia/Seoul")).toLocalTime().isBefore(LocalTime.of(15, 15))){
            throw new FCFSNotStartedYet("퀴즈가 아직 시작되지 않았습니다");
        }//퀴즈가 아직 시작 안되었다면


        return  ResponseEntity.ok(UserQuizDTO.toDTO(todayQuiz));
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<UserEventTimeDTO> getEventTime(){
        EventCommon eventCommon = eventCommonRepository.findById(1L)
                .orElseThrow(() -> new NoSuchElementException("공통 이벤트 정보가 존재하지 않습니다."));

        return ResponseEntity.ok(UserEventTimeDTO.toDTO(eventCommon));
    }

    @Override
    public ResponseEntity<Map<String,String>> login(UserLightDTO userLightDTO) {
        UserLight userLight = userLightRepository.findById(1L)
                .orElseThrow(() -> new NoSuchElementException("유저 정보가 존재하지 않습니다"));

        if(!isUserLoginSuccess(userLight, userLightDTO)){
            throw new UserLoginFailException("아이디 혹은 비밀번호가 맞지 않습니다.");
        }

        Map<String,String> map = new HashMap<>();
        //로그인 성공시 토큰을 발급해서 준다
        //역할이 user인 토큰을 발급받는다
        map.put("accessToken", jwtTokenProvider.generateUserToken(userLight.getUserId()));

        return ResponseEntity.ok(map);
    }//가벼운 로그인으로 카카오 로그인 구현 후에는 실제로 쓰진 않을 예정

    @Override
    public ResponseEntity<UserPersonalityUrlDTO> submitPersonalityTest(List<UserPersonalityAnswerDTO> userPersonalityAnswerDTOList,
                                                                    String authorizationHeader){
        Team team = parsePersonalityAnswer(userPersonalityAnswerDTOList);

        UserPersonalityUrlDTO userPersonalityUrlDTO = new UserPersonalityUrlDTO();

        userPersonalityUrlDTO.setTeam(team);
        userPersonalityUrlDTO.setAccessToken(jwtTokenProvider.generateTokenWithTeam(team,authorizationHeader));
        userPersonalityUrlDTO.setUrl(encryptedId(jwtTokenProvider.getUserId(authorizationHeader)));

        User user = userRepository.findById(jwtTokenProvider.getUserId(authorizationHeader))
                .orElseThrow(() -> new NoSuchElementException("유저 정보가 잘못되었습니다"));

        if(user.getTeam()!=null){
            throw new UserAlreadyHasTeamException("유저가 이미 성격 유형 검사를 마쳤습니다");
        }

        setEventUser(user,EventId.Racing.getValue());//이벤트 참여자 명단에 넣어준다

        user.setTeam(team);
        userRepository.save(user);//계산된 팀 정보를 업데이트해준다

        return ResponseEntity.ok(userPersonalityUrlDTO);
    }



    @Override
    public ResponseEntity<Map<String,UserQuizStatus>> submitQuiz(UserQuizAnswerDTO answer, String authorizationHeader){

        Map<String,UserQuizStatus> map = new HashMap<>();
        User user = userRepository.findById(jwtTokenProvider.getUserId(authorizationHeader))
                .orElseThrow(() -> new NoSuchElementException("유저 정보가 잘못되었습니다"));

        setEventUser(user,EventId.Quiz.getValue());//이벤트 참여자 명단에 넣어준다

        Integer userAnswer = answer.getAnswer();//유저가 제출한 정답
        Long id = user.getId();

        quizBranch(userAnswer,id,map);//퀴즈 분기 처리 (정답,오답,이미 참가함, 마감)

        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map<String,String>> dummyToken(){

        Map<String,String> map = new HashMap<>();
        map.put("accessToken", jwtTokenProvider.generateUserToken("user"));

        return ResponseEntity.ok(map);
    }

    @Override
    public Map<String,String> kakaoLogin(UserKakaoInfoDTO userKakaoInfoDTO){

        String userName = userKakaoInfoDTO.getEmail();//userName은 이메일 입니다
        Map<String,String> map = new HashMap<>();

        Optional<User> user = userRepository.findByUserName(userName);
        if(user.isPresent()){
            map.put("accessToken", jwtTokenProvider.generateUserToken(userName));
            return map;
        }//이미 유저 정보가 저장되어 있다면

        userRepository.save(getNewUser(userKakaoInfoDTO.getNickname(), userName));
        //유저가 없다면, UserDB에 저장을 해주어야 한다

        map.put("accessToken", jwtTokenProvider.generateUserToken(userName));
        return map;
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<UserClickNumberDTO> getClickNumber(String authorizationHeader){

        Long userId = jwtTokenProvider.getUserId(authorizationHeader);
        //토큰에서 유저 Id를 꺼내온다

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new NoSuchElementException("잘못된 유저 정보입니다"));
        //유저 Id를 이용해서 User Repo에서 해당 User를 찾는다

        return ResponseEntity.ok(UserClickNumberDTO.toDTO(user));

    }

    @Override
    public ResponseEntity<Void> plusClickNumber(String url, HttpServletRequest request){

        // 쿠키에서 해당 URL에 대한 방문 여부 확인
        Cookie[] cookies = request.getCookies();
        boolean hasVisited = false;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("visited_" + url)) {
                    hasVisited = true;
                    break;
                }
            }
        }

        if(hasVisited){
            return ResponseEntity.ok().build();
        }//해당 url을 방문 한 적이 있다면 아무 것도 실행하지않고 종료시킨다

        Long userId = decryptedId(url);
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new NoSuchElementException("잘못된 유저 정보입니다"));
        //유저 Id를 이용해서 User Repo에서 해당 User를 찾는다

        //해당 userId의 clickNumber를 증가시켜준다
        user.setClickNumber(user.getClickNumber()+1);

        // ResponseCookie를 사용하여 방문 쿠키 생성
        ResponseCookie visitCookie = ResponseCookie.from("visited_" + url, "true")
                .maxAge(60 * 60 * 24)  // 1일 동안 유지
                .path("/")              // 쿠키 경로 설정
                .httpOnly(true)         // XSS 공격 방지
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, visitCookie.toString())
                .build();
    }


    @Override
    public ResponseEntity<UserInfoDTO> getUserInfo(String authorizationHeader){
        Long userId = jwtTokenProvider.getUserId(authorizationHeader);
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new NoSuchElementException("잘못된 유저 정보입니다"));
        //유저 Id를 이용해서 User Repo에서 해당 User를 찾는다

        UserInfoDTO userInfoDTO = UserInfoDTO.toDTO(user);

        if(userInfoDTO.getTeam()==null){
            userInfoDTO.setUrl(null);
        } else{
            userInfoDTO.setUrl(encryptedId(userId));
        }//team이 null일 때는 url도 null, 아니라면 암호화된 url을 내려준다

        return ResponseEntity.ok(userInfoDTO);
    }

    public void setQuizAvailableArray(ArrayList<Boolean> availableArray){
        isQuizAvailable = availableArray;
    }

    private Team parsePersonalityAnswer(List<UserPersonalityAnswerDTO> userPersonalityAnswerDTOList){

        int petScore = 0;
        int travelScore = 0;
        int leisureScore = 0;
        int spaceScore = 0;

        for(UserPersonalityAnswerDTO dto : userPersonalityAnswerDTOList){
            Long id = dto.getId();
            Integer answer = dto.getAnswer();

            TeamScore teamScore = calculatePersonality(id, answer);

            petScore += teamScore.getPetScore();
            travelScore += teamScore.getTravelScore();
            leisureScore += teamScore.getLeisureScore();
            spaceScore += teamScore.getSpaceScore();
        }

        return determineTeam(petScore, travelScore, leisureScore, spaceScore);
    }//유저가 성격유형검사를 제출 했을 때 제출된 결과를 바탕으로 어느 팀에 속해 있을지 정해준다

    private TeamScore calculatePersonality(Long id, Integer answer){
        PersonalityTest personalityTest = personalityTestRepository.findById(id).
                orElseThrow(()->new NoSuchElementException("해당 성격 유형검사 문제에 대한 정보를 찾을 수 없습니다"));

        return (answer == 0) ? personalityTest.getChoice1Scores() : personalityTest.getChoice2Scores();
    }//해당 문제의 성격 점수를 계산해준다

    private Team determineTeam(int petScore, int travelScore, int leisureScore, int spaceScore) {
        int maxScore = Arrays.stream(new int[]{petScore, travelScore, leisureScore, spaceScore}).max().orElseThrow();

        if (maxScore == petScore) {
            return Team.PET;
        } else if (maxScore == travelScore) {
            return Team.TRAVEL;
        } else if (maxScore == leisureScore) {
            return Team.LEISURE;
        } else {
            return Team.SPACE;
        }
    }//주어진 4개의 점수를 가지고 어느 팀인지 판단

    private void quizBranch(Integer userAnswer, Long id, Map<String, UserQuizStatus> map){

        //LocalDate 한국 날짜를 기준으로 오늘의 퀴즈를 받아온다
        Quiz todayQuiz = quizRepository.findByPostDate(LocalDate.now(ZoneId.of("Asia/Seoul")))
                .orElseThrow(() -> new NoSuchElementException("오늘 날짜에 해당하는 퀴즈 이벤트가 존재하지 않습니다."));

        EventUser eventUser = eventUserRepository.findByUserIdAndEventId(id, EventId.Quiz.getValue());

        if(quizWinnerRepository.findByQuiz_IdAndEventUser_Id(todayQuiz.getId(), eventUser.getId()).isPresent()){
            map.put("status", UserQuizStatus.PARTICIPATED);
            return ;
        }//오늘 퀴즈에 이미 당첨이 되어있다면

        if(!userAnswer.equals(todayQuiz.getCorrectAnswer())){
            map.put("status", UserQuizStatus.WRONG);
            return;
        }//유저의 답변이 퀴즈 정답과 일치하지 않을 시

        int quizId = Integer.parseInt(todayQuiz.getId().toString());

        if(!isQuizAvailable.get(quizId)){
            map.put("status", UserQuizStatus.END);
            return;
        }//이미 마감되어 있다면

        if(redisTemplate.opsForValue().decrement("ticket_"+todayQuiz.getId())<0){
            isQuizAvailable.set(quizId,false);
            map.put("status", UserQuizStatus.END);
            return ;
        }// 티켓을 하나 뻇을때 -1이 나온다면 종료 시킨다

        QuizWinner quizWinner = new QuizWinner();
        quizWinner.setQuiz(todayQuiz);
        quizWinner.setEventUser(eventUser);
        quizWinnerRepository.save(quizWinner);

        map.put("status", UserQuizStatus.RIGHT);
    }

    private User getNewUser(String nickName, String userName){
        User newUser = new User();
        newUser.setLoginType(LoginType.KAKAO);
        newUser.setClickNumber(0);
        newUser.setIsMarketingAgree(true);
        newUser.setPhoneNumber("11111");
        newUser.setNickName(nickName);
        newUser.setUserName(userName);

        return newUser;
    }

    private String encryptedId(Long id){

        // XOR 연산
        long encrypted = id ^ key;

        // 암호화된 값을 16진수 문자열로 변환
        return Long.toHexString(encrypted);
    }

    private Long decryptedId(String url){

        long encrypted = Long.parseLong(url, 16);

        // XOR 복호화
        return encrypted ^ key;
    }
    private void setEventUser(User user,Long eventId) {
        EventUser eventUser = eventUserRepository.findByUserIdAndEventId(user.getId(),eventId);

        if(eventUser==null){
            eventUser = new EventUser();
            eventUser.setEvent(eventRepository.findById(eventId)
                    .orElseThrow(()->new NoSuchElementException("이벤트 테이블에 레이싱 이벤트가 존재하지 않습니다")));
            eventUser.setUser(user);
            eventUserRepository.save(eventUser);
        }//이벤트 참여자명단에 없으면 넣어준다.

    }

    private boolean isUserLoginSuccess(UserLight userLight, UserLightDTO dto){
        if(!userLight.getUserId().equals(dto.getUserId())) return false;
        if(!userLight.getPassword().equals(dto.getPassword())) return false;
        return true;
    }
}
