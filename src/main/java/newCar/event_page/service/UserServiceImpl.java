package newCar.event_page.service;

import lombok.RequiredArgsConstructor;
import newCar.event_page.exception.AdminLoginFailException;
import newCar.event_page.exception.UserLoginFailException;
import newCar.event_page.jwt.JwtTokenProvider;
import newCar.event_page.model.dto.user.*;
import newCar.event_page.model.entity.Team;
import newCar.event_page.model.entity.UserLight;
import newCar.event_page.model.entity.event.Event;
import newCar.event_page.model.entity.event.EventCommon;
import newCar.event_page.model.entity.event.quiz.Quiz;
import newCar.event_page.model.session.Session;
import newCar.event_page.model.session.UserSession;
import newCar.event_page.repository.jpa.EventCommonRepository;
import newCar.event_page.repository.jpa.EventRepository;
import newCar.event_page.repository.jpa.UserLightRepository;
import newCar.event_page.repository.jpa.quiz.QuizRepository;
import newCar.event_page.repository.jpa.racing.PersonalityTestRepository;
import newCar.event_page.repository.redis.SessionRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
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
    public ResponseEntity<Map<String,Object>> personalityTest(UserPersonalityAnswerDTO userPersonalityAnswerDTO){
        Team team = parsePersonalityAnswer(userPersonalityAnswerDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, jwtTokenProvider.generateTokenWithTeam());

        Map<String,Object> map = new HashMap<>();
        map.put("team",team);
        map.put("accessToken",jwtTokenProvider.generateTokenWithTeam());

        return ResponseEntity.ok(map);
    }

    private Team parsePersonalityAnswer(UserPersonalityAnswerDTO userPersonalityAnswerDTO){
        return Team.PET;
    }

    private boolean isUserLoginSuccess(UserLight userLight, UserLightDTO dto){
        if(!userLight.getUserId().equals(dto.getUserId())) return false;
        if(!userLight.getPassword().equals(dto.getPassword())) return false;
        return true;
    }
}
