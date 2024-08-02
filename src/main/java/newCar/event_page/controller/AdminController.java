package newCar.event_page.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import newCar.event_page.dto.*;
import newCar.event_page.entity.event.EventId;
import newCar.event_page.service.EventService;
import newCar.event_page.service.QuizService;
import newCar.event_page.service.RacingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@Tag(name = "admin API", description = "admin API 설계입니다")
@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final EventService eventService;
    private final QuizService quizService;
    private final RacingService racingService;

    @GetMapping("/common-event") //이벤트 관리 버튼(이벤트 공통, 선착순 퀴즈, 캐스퍼 레이싱 설정값 불러옴)
    @Operation(summary = "이벤트명, 상태, 담당자, 진행기간", description = "https://www.figma.com/design/HhnC3JbEYv2qqQaP6zdhnI?node-id=2355-435#886120115")
    public ResponseEntity<EventCommonDTO> getCommonEvent() {
        return ResponseEntity.ok(eventService.getEventInfo());
    }

    @PostMapping("/common-event")
    @Operation(summary = "이벤트명, 상태, 담당자, 진행기간 수정", description = "https://www.figma.com/design/HhnC3JbEYv2qqQaP6zdhnI?node-id=2355-435#886180035")
    public ResponseEntity<EventCommonDTO> updateCommonEvent(@Validated @RequestBody EventCommonDTO eventCommonDTO) {
        return ResponseEntity.ok(eventService.updateEventInfo(eventCommonDTO));
    }

    @GetMapping("/quiz-list")
    @Operation(summary = "선착순 퀴즈 이벤트 정보", description = "https://www.figma.com/design/HhnC3JbEYv2qqQaP6zdhnI?node-id=2355-4#887413777")
    public ResponseEntity<List<QuizDTO>> getQuizList() {
        return ResponseEntity.ok(quizService.getQuizList(EventId.Quiz.getValue()));
    }

    @PostMapping("/quiz") //선착순퀴즈 수정 버튼
    @Operation(summary = "선착순퀴즈 이벤트 수정버튼", description = "https://www.figma.com/design/HhnC3JbEYv2qqQaP6zdhnI?node-id=2355-4#887450213")
    public ResponseEntity<QuizDTO> updateQuiz(@Validated @RequestBody QuizDTO quizDTO) {
        return ResponseEntity.ok(quizService.updateQuiz(quizDTO));
    }


    @PostMapping("/racing-winners")//당첨자 추첨하기 버튼
    @Operation(summary = "캐스퍼 레이싱 당첨자 추첨하기 버튼", description = "https://www.figma.com/design/HhnC3JbEYv2qqQaP6zdhnI?node-id=2355-702#886184643")
    public ResponseEntity<String> drawWinners(@Validated @RequestBody List<WinnerSettingDTO> winnerSettingDTOList) {
        if (isDrawingAvailable(winnerSettingDTOList)) {
            racingService.drawWinners(winnerSettingDTOList, EventId.Racing.getValue());
            return new ResponseEntity<>("HTTP 200 OK", HttpStatus.OK);
        }
        return new ResponseEntity<>("추첨하려는 총 인원이 참가자보다 많습니다", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/racing-winners") //당첨자 목록 버튼
    @Operation(summary = "캐스퍼 레이싱 당첨자 목록", description = "https://www.figma.com/design/HhnC3JbEYv2qqQaP6zdhnI?node-id=2355-1024#887658590")
    public ResponseEntity<?> getWinnerList() {
        List<RacingWinnersDTO> winnersDTOList = racingService.getWinnerList(EventId.Racing.getValue());
        if (winnersDTOList.isEmpty()) {//당첨자 추첨이 이뤄지지 않았을 경우 당첨자 테이블이 비어있다
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("당첨자 추첨이 아직 이뤄지지 않았습니다");
        }
        return ResponseEntity.ok(winnersDTOList);
    }

    @GetMapping("/personality-test-list")
    @Operation(summary = "레이싱 게임 유형검사", description = "https://www.figma.com/design/HhnC3JbEYv2qqQaP6zdhnI?node-id=2355-211#887801621")
    public ResponseEntity<List<PersonalityTestDTO>> getPersonalities() {
        return ResponseEntity.ok(racingService.getPersonalityList());
    }

    @PostMapping("/personality-test") //유형 검사 질문박스 수정
    public ResponseEntity<PersonalityTestDTO> updatePersonality(@Validated @RequestBody PersonalityTestDTO personalityTestDTO) {
        return ResponseEntity.ok(racingService.updatePersonalityTest(personalityTestDTO));
    }

    private boolean isDrawingAvailable(List<WinnerSettingDTO> winnerSettingDTOList) {
        int size = racingService.getEventUserSize(EventId.Racing.getValue());//현재 참가자 수
        int drawNum = 0; // 추첨할 숫자
        for (WinnerSettingDTO winnerSettingDTO : winnerSettingDTOList) {
            drawNum += winnerSettingDTO.getNum();
        }
        return size >= drawNum;
    }//추첨이 가능한지 확인하는 메소드
}
