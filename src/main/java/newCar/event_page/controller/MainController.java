package newCar.event_page.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import newCar.event_page.model.dto.user.UserEventTimeDTO;
import newCar.event_page.model.dto.user.UserLightDTO;
import newCar.event_page.model.dto.user.UserPersonalityTestDTO;
import newCar.event_page.model.dto.user.UserQuizDTO;
import newCar.event_page.model.entity.event.EventId;
import newCar.event_page.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Tag(name = "Main API", description = "Main API 설계입니다")
@RestController
@RequestMapping("/main")
@CrossOrigin("*")
public class MainController {

    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "유저 로그인")
    public ResponseEntity<String> userLogin(@Valid @RequestBody UserLightDTO userLightDTO) {
        return userService.login(userLightDTO);
    }
    @GetMapping("/abc")
    public ResponseEntity<String> test(){
        return ResponseEntity.ok("테스트용");
    }

    @GetMapping("/event-time")
    @Operation(summary = "이벤트 진행 기간을 startTime, endTime 으로 반환한다")
    public ResponseEntity<UserEventTimeDTO> getEventTime(){
        return ResponseEntity.ok(userService.getEventTime());
    }

    @GetMapping("/personality-test-list")
    @Operation(summary = "레이싱 게임 유형검사")
    public ResponseEntity<List<UserPersonalityTestDTO>> getPersonalities() {
        return ResponseEntity.ok(userService.getUserPersonalityTestList());
    }

    @GetMapping("/quiz")
    @Operation(summary = "해당하는 날짜에 맞는 선착순 퀴즈 정보를 Get 한다")
    public ResponseEntity<UserQuizDTO> getQuiz(){
        return ResponseEntity.ok(userService.getQuiz(EventId.Quiz.getValue()));
    }


}
