package newCar.event_page.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import newCar.event_page.model.dto.user.*;
import newCar.event_page.model.entity.event.EventId;
import newCar.event_page.model.enums.UserQuizStatus;
import newCar.event_page.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Tag(name = "Main API", description = "Main API 설계입니다")
@RestController
@RequestMapping("/main")
public class MainController {

    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "단순 유저 로그인")
    public ResponseEntity<Map<String,String>> userLogin(@Valid @RequestBody UserLightDTO userLightDTO) {
        return userService.login(userLightDTO);
    }

    @GetMapping("/event-time")
    @Operation(summary = "이벤트 진행 기간을 startTime, endTime 으로 반환한다")
    public ResponseEntity<UserEventTimeDTO> getEventTime(){
        return userService.getEventTime();
    }

    @GetMapping("/personality-test-list")
    @Operation(summary = "레이싱 게임 유형검사")
    public ResponseEntity<List<UserPersonalityTestDTO>> getPersonalities() {
        return userService.getPersonalityTestList();
    }

    @GetMapping("/quiz")
    @Operation(summary = "해당하는 날짜에 맞는 선착순 퀴즈 정보를 Get 한다")
    public ResponseEntity<UserQuizDTO> getQuiz(){
        return userService.getQuiz(EventId.Quiz.getValue());
    }

    @PostMapping("/personality-test")
    @Operation(summary = "성격 유형 검사 풀고 제출시")
    public ResponseEntity<Map<String, Object>> personalityTestAnswer(@Valid @RequestBody List<UserPersonalityAnswerDTO> userPersonalityAnswerDTOList,
                                                                     @RequestHeader("Authorization") String authorizationHeader) {
        return userService.submitPersonalityTest(userPersonalityAnswerDTOList, authorizationHeader);
    }

    @PostMapping("/quiz-user")
    @Operation(summary = "유저가 선착순 퀴즈 풀고 제출시")
    public ResponseEntity<Map<String,UserQuizStatus>> quizSubmission(@Valid @RequestBody UserQuizAnswerDTO answer, @RequestHeader("Authorization") String authorizationHeader){
        return userService.submitQuiz(answer, authorizationHeader);
    }

    @GetMapping("/click-number")
    @Operation(summary = "유저의 공유 링크 클릭 수 ")
    public ResponseEntity<UserClickNumberDTO> getClickNumber(@RequestHeader("Authorization") String authorizationHeader){
        return userService.getClickNumber(authorizationHeader);
    }

    @GetMapping("/share-link")
    @Operation(summary = "유저가 생성해낸 공유 링크를 클릭 했을 때")
    public ResponseEntity<Void> plusClickNumber(@RequestParam("userId") Long userId) {

        return userService.plusClickNumber(userId);
    }

    @GetMapping("/dummy-token")
    public ResponseEntity<Map<String,String>> dummyToken(){
        return userService.dummyToken();
    }

}
