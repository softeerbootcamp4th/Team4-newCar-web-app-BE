package newCar.event_page.service;

import lombok.RequiredArgsConstructor;
import newCar.event_page.exception.UserLoginFailException;
import newCar.event_page.jwt.JwtTokenProvider;
import newCar.event_page.model.dto.user.*;
import newCar.event_page.model.entity.Team;
import newCar.event_page.model.entity.TeamScore;
import newCar.event_page.model.entity.User;
import newCar.event_page.model.entity.UserLight;
import newCar.event_page.model.entity.event.Event;
import newCar.event_page.model.entity.event.EventCommon;
import newCar.event_page.model.entity.event.quiz.Quiz;
import newCar.event_page.model.entity.event.racing.PersonalityTest;
import newCar.event_page.repository.jpa.EventCommonRepository;
import newCar.event_page.repository.jpa.EventRepository;
import newCar.event_page.repository.jpa.UserLightRepository;
import newCar.event_page.repository.jpa.UserRepository;
import newCar.event_page.repository.jpa.quiz.QuizRepository;
import newCar.event_page.repository.jpa.racing.PersonalityTestRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserLightRepository userLightRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PersonalityTestRepository personalityTestRepository;
    private final EventRepository eventRepository;
    private final QuizRepository quizRepository;
    private final EventCommonRepository eventCommonRepository;

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserPersonalityTestDTO> getPersonalityTestList() {
        return personalityTestRepository.findAllByOrderByIdAsc()
                .stream()
                .map(UserPersonalityTestDTO::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserQuizDTO getQuiz(Long quizEventId ){
        Event quizEvent = eventRepository.findById(quizEventId)
                .orElseThrow(() -> new NoSuchElementException("퀴즈 이벤트가 존재하지 않습니다."));

        //LocalDate 한국 날짜를 기준으로 오늘의 퀴즈를 받아온다
        Quiz todayQuiz = quizRepository.findByPostDate(LocalDate.now(ZoneId.of("Asia/Seoul")))
                .orElseThrow(() -> new NoSuchElementException("오늘 날짜에 해당하는 퀴즈 이벤트가 존재하지 않습니다."));

        return UserQuizDTO.toDTO(todayQuiz);
    }

    @Override
    @Transactional(readOnly = true)
    public UserEventTimeDTO getEventTime(){
        EventCommon eventCommon = eventCommonRepository.findById(1L)
                .orElseThrow(() -> new NoSuchElementException("공통 이벤트 정보가 존재하지 않습니다."));

        return UserEventTimeDTO.toDTO(eventCommon);
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
    public ResponseEntity<Map<String,Object>> personalityTest(List<UserPersonalityAnswerDTO> userPersonalityAnswerDTOList,
                                                              String authorizationHeader){
        Team team = parsePersonalityAnswer(userPersonalityAnswerDTOList);

        Map<String,Object> map = new HashMap<>();
        map.put("team",team);
        map.put("accessToken",jwtTokenProvider.generateTokenWithTeam(team,authorizationHeader));

        User user = userRepository.findById(jwtTokenProvider.getUserId(authorizationHeader))
                .orElseThrow(() -> new NoSuchElementException("유저 정보가 잘못되었습니다"));

        user.setTeam(team);
        userRepository.save(user);//계산된 팀 정보를 업데이트해준다

        return ResponseEntity.ok(map);
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
        int maxScore = findMax(petScore, travelScore, leisureScore, spaceScore);

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

    private int findMax(int a,int b,int c, int d){
        int max1 = Math.max(a, b);
        int max2 = Math.max(c, d);
        return Math.max(max1, max2);
    } // 4값 중 가장 큰 값을 찾는 메소드

    private boolean isUserLoginSuccess(UserLight userLight, UserLightDTO dto){
        if(!userLight.getUserId().equals(dto.getUserId())) return false;
        if(!userLight.getPassword().equals(dto.getPassword())) return false;
        return true;
    }
}
