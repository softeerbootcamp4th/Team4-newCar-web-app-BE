package newCar.event_page.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import newCar.event_page.exception.FCFS.FCFSFinishedException;
import newCar.event_page.exception.FCFS.FCFSNotStartedYet;
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
    public ResponseEntity<UserQuizDTO> getQuiz(Long quizEventId ){
        Event quizEvent = eventRepository.findById(quizEventId)
                .orElseThrow(() -> new NoSuchElementException("퀴즈 이벤트가 존재하지 않습니다."));

        //LocalDate 한국 날짜를 기준으로 오늘의 퀴즈를 받아온다
        Quiz todayQuiz = quizRepository.findByPostDate(LocalDate.now(ZoneId.of("Asia/Seoul")))
                .orElseThrow(() -> new NoSuchElementException("오늘 날짜에 해당하는 퀴즈 이벤트가 존재하지 않습니다."));

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
    }

    @Override
    public ResponseEntity<Map<String,Object>> submitPersonalityTest(List<UserPersonalityAnswerDTO> userPersonalityAnswerDTOList,
                                                                    String authorizationHeader){
        Team team = parsePersonalityAnswer(userPersonalityAnswerDTOList);
        System.out.println(team);
        Map<String,Object> map = new HashMap<>();
        map.put("team : ", team);
        map.put("accessToken", jwtTokenProvider.generateTokenWithTeam(team,authorizationHeader));


        User user = userRepository.findById(jwtTokenProvider.getUserId(authorizationHeader))
                .orElseThrow(() -> new NoSuchElementException("유저 정보가 잘못되었습니다"));

        user.setTeam(team);
        userRepository.save(user);//계산된 팀 정보를 업데이트해준다


        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map<String,UserQuizStatus>> submitQuiz(UserQuizAnswerDTO answer, String token){

        Map<String,UserQuizStatus> map = new HashMap<>();
        Long id = jwtTokenProvider.getUserId(token);//유저 토큰에서 유저 아이디를 받아온다

        EventUser eventUser = eventUserRepository.findByUserIdAndEventId(id, EventId.Quiz.getValue());

        if(eventUser==null){
            eventUser = new EventUser();
            eventUser.setEvent(eventRepository.findById(EventId.Quiz.getValue())
                    .orElseThrow(()->new NoSuchElementException("이벤트 테이블에 퀴즈이벤트가 존재하지 않습니다")));
            eventUser.setUser(userRepository.findById(id)
                        .orElseThrow(()-> new NoSuchElementException("유저 정보가 없습니다")));
            eventUserRepository.save(eventUser);
        }//퀴즈 이벤트 참여자 명단에 없으면 넣어준다


        Integer userAnswer = answer.getAnswer();//유저가 제출한 정답

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
    public Map<String,String> kakaoLogin(Map<String,String> userInfo){

        String userName = userInfo.get("email");//userName은 이메일 입니다
        Map<String,String> map = new HashMap<>();

        Optional<User> user = userRepository.findByUserName(userName);
        if(user.isPresent()){
            map.put("accessToken",jwtTokenProvider.generateUserToken(userName));
            return map;
        }//이미 유저 정보가 저장되어 있다면

        userRepository.save(getNewUser(userInfo.get("nickname"),userInfo.get(userName)));
        //유저가 없다면, UserDB에 저장을 해주어야 한다

        map.put("accessToken", jwtTokenProvider.generateUserToken(userName));

        return map;
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

            TeamScore teamScore = calculatePersonality(id,answer);

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

    private void quizBranch(Integer userAnswer,Long id,Map<String,UserQuizStatus> map){

        //LocalDate 한국 날짜를 기준으로 오늘의 퀴즈를 받아온다
        Quiz todayQuiz = quizRepository.findByPostDate(LocalDate.now(ZoneId.of("Asia/Seoul")))
                .orElseThrow(() -> new NoSuchElementException("오늘 날짜에 해당하는 퀴즈 이벤트가 존재하지 않습니다."));

        EventUser eventUser = eventUserRepository.findByUserIdAndEventId(id,EventId.Quiz.getValue());

        if(quizWinnerRepository.findByQuiz_IdAndEventUser_Id(todayQuiz.getId(), eventUser.getId()).isPresent()){
            map.put("status",UserQuizStatus.PARTICIPATED);
            return ;
        }//오늘 퀴즈에 이미 당첨이 되어있다면

        if(!userAnswer.equals(todayQuiz.getCorrectAnswer())){
            map.put("status",UserQuizStatus.WRONG);
            return;
        }//유저의 답변이 퀴즈 정답과 일치하지 않을 시

        int quizId = Integer.parseInt(todayQuiz.getId().toString());

        if(!isQuizAvailable.get(quizId)){
            map.put("status",UserQuizStatus.END);
            return;
        }//이미 마감되어 있다면

        if(redisTemplate.opsForValue().decrement("ticket_"+todayQuiz.getId())<0){
            isQuizAvailable.set(quizId,false);
            map.put("status",UserQuizStatus.END);
            return ;
        }// 티켓을 하나 뻇을때 -1이 나온다면 종료 시킨다



        QuizWinner quizWinner = new QuizWinner();
        quizWinner.setQuiz(todayQuiz);
        quizWinner.setEventUser(eventUser);
        quizWinnerRepository.save(quizWinner);

        map.put("status",UserQuizStatus.RIGHT);
    }

    private User getNewUser(String nickName,String userName){
        User newUser = new User();
        newUser.setLoginType(LoginType.KAKAO);
        newUser.setClickNumber(0);
        newUser.setIsMarketingAgree(true);
        newUser.setPhoneNumber("11111");
        newUser.setNickName(nickName);
        newUser.setUserName(userName);

        return newUser;
    }

    private boolean isUserLoginSuccess(UserLight userLight, UserLightDTO dto){
        if(!userLight.getUserId().equals(dto.getUserId())) return false;
        if(!userLight.getPassword().equals(dto.getPassword())) return false;
        return true;
    }
}
