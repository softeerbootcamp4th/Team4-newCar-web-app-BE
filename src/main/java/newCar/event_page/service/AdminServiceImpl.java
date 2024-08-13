package newCar.event_page.service;

import lombok.RequiredArgsConstructor;
import newCar.event_page.exception.*;
import newCar.event_page.jwt.JwtTokenProvider;
import newCar.event_page.model.dto.admin.*;
import newCar.event_page.model.entity.Administrator;
import newCar.event_page.model.entity.event.Event;
import newCar.event_page.model.entity.event.EventCommon;
import newCar.event_page.model.entity.event.EventId;
import newCar.event_page.model.entity.event.EventUser;
import newCar.event_page.model.entity.event.quiz.Quiz;
import newCar.event_page.model.entity.event.quiz.QuizEvent;
import newCar.event_page.model.entity.event.quiz.QuizWinner;
import newCar.event_page.model.entity.event.racing.PersonalityTest;
import newCar.event_page.model.entity.event.racing.RacingWinner;
import newCar.event_page.repository.jpa.AdministratorRepository;
import newCar.event_page.repository.jpa.EventCommonRepository;
import newCar.event_page.repository.jpa.EventRepository;
import newCar.event_page.repository.jpa.EventUserRepository;
import newCar.event_page.repository.jpa.quiz.QuizEventRepository;
import newCar.event_page.repository.jpa.quiz.QuizRepository;
import newCar.event_page.repository.jpa.quiz.QuizWinnerRepository;
import newCar.event_page.repository.jpa.racing.PersonalityTestRepository;
import newCar.event_page.repository.jpa.racing.RacingEventRepository;
import newCar.event_page.repository.jpa.racing.RacingWinnerRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

    private final EventCommonRepository eventCommonRepository;
    private final EventRepository eventRepository;
    private final EventUserRepository eventUserRepository;

    private final QuizRepository quizRepository;
    private final QuizEventRepository quizEventRepository;
    private final QuizWinnerRepository quizWinnerRepository;

    private final RacingWinnerRepository racingWinnerRepository;
    private final RacingEventRepository racingEventRepository;
    private final PersonalityTestRepository personalityTestRepository;

    private final AdministratorRepository administratorRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final RedisTemplate<String,Object> redisTemplate;

    private double totalWeight;

    private final UserServiceImpl userServiceImpl;



    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<AdminEventCommonDTO> getCommonEventDetails() {
        EventCommon eventCommon = eventCommonRepository.findById(1L)
                .orElseThrow(() -> new NoSuchElementException("공통 이벤트 정보가 존재하지 않습니다."));

        return ResponseEntity.ok(AdminEventCommonDTO.toDTO(eventCommon));
    }

    @Override
    public ResponseEntity<AdminEventCommonDTO> updateCommonEventDetails(AdminEventCommonDTO eventCommonDTO) {
        EventCommon eventCommon = eventCommonRepository.findById(1L)
                .orElseThrow(() -> new NoSuchElementException("공통 이벤트 정보가 존재하지 않아 수정이 불가능합니다."));
        eventCommon.update(eventCommonDTO);

        long duration = eventCommon.getDuration();
        System.out.println("공통이벤트");
        putDummyIfRequired(duration);
        updateQuiz(eventCommonDTO.getStartTime().toLocalDate() , duration);

        userServiceImpl.isQuizAvailable = new ArrayList<>();//기간이 바뀌면 다시 설정


        for(int i = 0; i < duration; i++){
            userServiceImpl.isQuizAvailable.add(true);
        }
        List<Quiz> quizList = quizRepository.findAllByOrderByIdAsc();
        for(int i = 0 ; i< duration ;i++){
            Quiz quiz = quizList.get(i);
            redisTemplate.opsForValue().set("ticket_"+quiz.getId(), quiz.getWinnerCount());
        }

        return ResponseEntity.ok(AdminEventCommonDTO.toDTO(eventCommon));
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<List<AdminQuizDTO>> getQuizList(Long quizEventId) {
        Event quizEvent = eventRepository.findById(quizEventId)
                .orElseThrow(() -> new NoSuchElementException("퀴즈 이벤트가 존재하지 않습니다."));

        long eventDuration = quizEvent.getDuration();

        return ResponseEntity.ok(
                quizRepository.findAllByOrderByPostDateAsc()
                .stream()
                .map(AdminQuizDTO::toDTO)
                .limit(eventDuration)
                .toList());
    }

    @Override
    public ResponseEntity<AdminQuizDTO> updateQuiz(AdminQuizDTO adminQuizDTO) {
        Long id = adminQuizDTO.getId();
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(id + "- 존재하지 않는 퀴즈 ID입니다."));

        if(adminQuizDTO.getPostDate() != null){
            throw new UnmodifiableFieldException("게시 날짜는 변경할 수 없습니다.");
        }

        quiz.update(adminQuizDTO);
        redisTemplate.opsForValue().set("ticket_"+quiz.getId(), quiz.getWinnerCount());

        quizRepository.save(quiz);
        return ResponseEntity.ok(AdminQuizDTO.toDTO(quiz));
    }

    @Override
    public ResponseEntity<String> drawRacingWinners(List<AdminWinnerSettingDTO> winnerSettingDTOList, Long racingEventId) {
        List<EventUser> eventUserList = eventUserRepository.findByEventId(racingEventId); //Racing게임을 참가한 사람들의 목록을 받아온다

        int participantCount = eventUserList.size();

        int drawCount = winnerSettingDTOList
                .stream()
                .mapToInt(AdminWinnerSettingDTO::getNum)
                .sum();

        if(participantCount < drawCount) {
            throw new ExcessiveWinnersRequestedException(
                    "추첨하려는 인원이 참가자 수보다 많습니다." +
                            "추첨 인원 : " + drawCount +
                            " 참가자 수 : " + participantCount
            );
        }

        racingWinnerRepository.deleteByEventId(racingEventId);

        Set<Participant> participantSet = new LinkedHashSet<>();
        totalWeight = 0;
        double weight = 0;
        for (EventUser eventUser : eventUserList) {
            weight = getWeight(eventUser.getUser().getClickNumber());
            participantSet.add(new Participant(eventUser.getUser().getId(), weight));
            totalWeight += weight;
        }//각 참가자의 가중치와 전체 가중치를 구해 준다
        setWinners(winnerSettingDTOList, racingEventId, participantSet);
        return ResponseEntity.ok("추첨이 완료되었습니다.");
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<List<AdminRacingWinnersDTO>> getRacingWinnerList(Long racingEventId) {
        List<RacingWinner> winnerList = racingWinnerRepository.findByEventId(racingEventId);
        if(winnerList.isEmpty()) {
            throw new DrawNotYetConductedException("당첨자 추첨이 아직 이루어지지 않았습니다.");
        }

        return ResponseEntity.ok(
                winnerList
                        .stream()
                        .map(AdminRacingWinnersDTO::toDTO)
                        .toList()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<List<AdminPersonalityTestDTO>> getPersonalityTestList() {
        return ResponseEntity.ok(
                personalityTestRepository.findAllByOrderByIdAsc()
                        .stream()
                        .map(AdminPersonalityTestDTO::toDTO)
                        .toList());
    }

    @Override
    public ResponseEntity<AdminPersonalityTestDTO> updatePersonalityTest(AdminPersonalityTestDTO personalityTestDTO) {
        Long id = personalityTestDTO.getId();
        PersonalityTest personalityTest = personalityTestRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(id + "- 존재하지 않는 유형검사 ID입니다."));

        personalityTest.update(personalityTestDTO);
        personalityTestRepository.save(personalityTest);

        return ResponseEntity.ok(AdminPersonalityTestDTO.toDTO(personalityTest));
    }

    @Override
    public ResponseEntity<List<AdminQuizWinnersDTO>> getQuizWinnerList(Long quizEventId){
        List<QuizWinner> quizWinnerList = quizWinnerRepository.findAllByOrderByQuiz_Id();
        if(quizWinnerList.isEmpty()){
            throw new FCFSNotYetConductedException("아직 선착순 퀴즈 당첨자가 존재하지 않습니다");
        }

        return ResponseEntity.ok(quizWinnerList
                .stream()
                .map(AdminQuizWinnersDTO::toDTO)
                .toList());
    }

    @Override
    public ResponseEntity<Map<String , String>> login(AdminLoginDTO adminLoginDTO) {
        Administrator administrator = administratorRepository.findById(1L)
                .orElseThrow(() -> new NoSuchElementException("관리자 정보가 존재하지 않습니다."));

        if(!isAdminLoginSuccess(administrator, adminLoginDTO)){
            throw new AdminLoginFailException("아이디 혹은 비밀번호가 맞지 않습니다.");
        }

        Map<String, String> map = new HashMap<>();
        map.put("accessToken", jwtTokenProvider.generateAdminToken());
        //로그인 성공시 토큰을 발급해서 준다

        return ResponseEntity.ok(map);
    }





    private void putDummyIfRequired(long duration) {
        long quizCount = quizRepository.count();

        if(quizCount > duration) return;

        QuizEvent quizEvent = quizEventRepository.findById(EventId.Quiz.getValue())
                .orElseThrow(() -> new NoSuchElementException("퀴즈 이벤트가 존재하지 않습니다."));

        for(int i = 0; i < duration - quizCount; i++){
            quizRepository.save(Quiz.createDummy(quizEvent));
        }
    }

    private void updateQuiz(LocalDate startDate, long duration) {
        List<Quiz> quizList = quizRepository.findAllByOrderByIdAsc();

        for (Quiz quiz : quizList) {
            quiz.setPostDate(startDate);
            startDate = startDate.plusDays(1L);
        }

        quizRepository.saveAll(quizList);
    }

    private void setWinners(List<AdminWinnerSettingDTO> winnerSettingDTOList, Long eventId, Set<Participant> participantSet) {
        for (AdminWinnerSettingDTO winnerSettingDTO : winnerSettingDTOList) {
            int numberOfWinners = winnerSettingDTO.getNum();// 각 등수별로 몇명 뽑는지 넘어온 설정 값으로 참조
            int rank = winnerSettingDTO.getRank(); //이번 추첨은 어떤 랭크인지
            for (int i = 0; i < numberOfWinners; i++) {
                Participant winner = drawOneWinner(participantSet,totalWeight);
                totalWeight -= winner.weight; //전체가중치에서 당첨자의 가중치를 빼준다
                participantSet.remove(winner);//중복 제거를 위해 Set에서 제거 해준다
                racingWinnerRepository.save(new RacingWinner(racingEventRepository.getReferenceById(eventId),
                        eventUserRepository.findByUserIdAndEventId(winner.userId, eventId),rank));
            }
        }
    }

    private Participant drawOneWinner(Set<Participant> participantSet, double totalWeight){
        Random rand = new Random();
        int maxInt = (int)totalWeight; //totalWeight가 8.45일 경우 8이 저장됨
        int randomInt = rand.nextInt(maxInt) + 1; // 1~8이 저장됨
        if (randomInt == maxInt) { //만약 randomInt가 최대값으로 들어왔다면 마지막이 당첨자이다
            Participant participant = findLast(participantSet);//참가자리스트의 마지막
            return participant;
        }
        double cumulativeWeight = 0; // 누적 가중치
        double randomValue = randomInt + rand.nextDouble(); //랜덤 값
        for (Participant participant : participantSet) {
            cumulativeWeight += participant.weight;
            if (randomValue <= cumulativeWeight) {
                return participant;
            }
        }
        return null;
    }

    private Participant findLast(Set<Participant> participantSet){
        Participant lastElement = null;
        //위에서 선언한 set변수
        for (Participant participant : participantSet) {
            lastElement = participant;
        }
        return lastElement;
    }

    private double getWeight(int clickNumber) {
        return 1 + ((Math.log(clickNumber+1))/(Math.log(30)));
    }

    class Participant {
        public Long userId;
        public double weight;
        public Participant(Long userId, double weight) {
            this.userId=userId;
            this.weight=weight;
        }
    }

    private boolean isAdminLoginSuccess(Administrator admin, AdminLoginDTO dto){
        if(!admin.getAdminId().equals(dto.getAdminId())) return false;
        if(!admin.getPassword().equals(dto.getPassword())) return false;
        return true;
    }
}
