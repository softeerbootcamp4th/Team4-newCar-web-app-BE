package newCar.event_page.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import newCar.event_page.model.dto.admin.*;
import newCar.event_page.model.entity.event.EventId;
import newCar.event_page.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Tag(name = "admin API", description = "admin API 설계입니다")
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/common-event") //이벤트 관리 버튼(이벤트 공통, 선착순 퀴즈, 캐스퍼 레이싱 설정값 불러옴)
    @Operation(summary = "이벤트명, 상태, 담당자, 진행기간", description = "https://www.figma.com/design/HhnC3JbEYv2qqQaP6zdhnI?node-id=2355-435#886120115")
    public ResponseEntity<AdminEventCommonDTO> getCommonEvent() {
        return adminService.getCommonEventDetails();
    }

    @PostMapping("/common-event")
    @Operation(summary = "이벤트명, 상태, 담당자, 진행기간 수정", description = "https://www.figma.com/design/HhnC3JbEYv2qqQaP6zdhnI?node-id=2355-435#886180035")
    public ResponseEntity<AdminEventCommonDTO> updateCommonEvent(@Valid @RequestBody AdminEventCommonDTO adminEventCommonDTO) {
        return adminService.updateCommonEventDetails(adminEventCommonDTO);
    }

    @GetMapping("/quiz-list")
    @Operation(summary = "선착순 퀴즈 이벤트 정보", description = "https://www.figma.com/design/HhnC3JbEYv2qqQaP6zdhnI?node-id=2355-4#887413777")
    public ResponseEntity<List<AdminQuizDTO>> getQuizList() {
        return adminService.getQuizList(EventId.Quiz.getValue());
    }

    @PostMapping("/quiz") //선착순퀴즈 수정 버튼
    @Operation(summary = "선착순퀴즈 이벤트 수정버튼", description = "https://www.figma.com/design/HhnC3JbEYv2qqQaP6zdhnI?node-id=2355-4#887450213")
    public ResponseEntity<AdminQuizDTO> updateQuiz(@Valid @RequestBody AdminQuizDTO adminQuizDTO) {
        return adminService.updateQuiz(adminQuizDTO);
    }

    @PostMapping("/racing-winners")//당첨자 추첨하기 버튼
    @Operation(summary = "캐스퍼 레이싱 당첨자 추첨하기 버튼", description = "https://www.figma.com/design/HhnC3JbEYv2qqQaP6zdhnI?node-id=2355-702#886184643")
    public ResponseEntity<String> drawWinners(@Valid @RequestBody List<AdminWinnerSettingDTO> winnerSettingDTOList) {
        return adminService.drawRacingWinners(winnerSettingDTOList, EventId.Racing.getValue());
    }

    @GetMapping("/racing-winners") //당첨자 목록 버튼
    @Operation(summary = "캐스퍼 레이싱 당첨자 목록", description = "https://www.figma.com/design/HhnC3JbEYv2qqQaP6zdhnI?node-id=2355-1024#887658590")
    public ResponseEntity<List<AdminRacingWinnersDTO>> getWinnerList() {
        return adminService.getRacingWinnerList(EventId.Racing.getValue());
    }

    @GetMapping("/personality-test-list")
    @Operation(summary = "레이싱 게임 유형검사", description = "https://www.figma.com/design/HhnC3JbEYv2qqQaP6zdhnI?node-id=2355-211#887801621")
    public ResponseEntity<List<AdminPersonalityTestDTO>> getPersonalities() {
        return adminService.getPersonalityTestList();
    }

    @PostMapping("/personality-test") //유형 검사 질문박스 수정
    public ResponseEntity<AdminPersonalityTestDTO> updatePersonality(@Valid @RequestBody AdminPersonalityTestDTO adminPersonalityTestDTO) {
        return adminService.updatePersonalityTest(adminPersonalityTestDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> adminLogin(@Valid @RequestBody AdminLoginDTO adminLoginDTO) {
        return adminService.login(adminLoginDTO);
    }

}
