package newCar.event_page.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import newCar.event_page.dto.*;
import newCar.event_page.entity.event.EventId;
import newCar.event_page.service.EventService;
import newCar.event_page.service.QuizService;
import newCar.event_page.service.RacingService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Tag(name = "admin API" , description = "admin API 설계입니다")
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final EventService eventService;
    private final QuizService quizService;
    private final RacingService racingService;

    @Autowired
    public AdminController(EventService eventService,QuizService quizService,RacingService racingService)
    {
        this.eventService=eventService;
        this.quizService=quizService;
        this.racingService=racingService;
    }

    @GetMapping("/common-event") //이벤트 관리 버튼(이벤트 공통, 선착순 퀴즈, 캐스퍼 레이싱 설정값 불러옴)
    @Operation (summary = "이벤트명, 상태, 담당자, 진행기간", description = "https://www.figma.com/design/HhnC3JbEYv2qqQaP6zdhnI?node-id=2355-435#886120115")
    public EventCommonDTO getCommonEvent(){
        return eventService.getEventInfo();
    }

    @PostMapping("/common-event")
    @Operation (summary = "이벤트명, 상태, 담당자, 진행기간 수정", description = "https://www.figma.com/design/HhnC3JbEYv2qqQaP6zdhnI?node-id=2355-435#886180035")
    public EventCommonDTO updateCommonEvent(@Validated @RequestBody EventCommonDTO eventCommonDTO){
        return eventService.updateEventInfo(eventCommonDTO);
    }


    @GetMapping("/quiz")
    @Operation( summary = "선착순 퀴즈 이벤트 정보", description= "https://www.figma.com/design/HhnC3JbEYv2qqQaP6zdhnI?node-id=2355-4#887413777")
    public List<QuizDTO> getQuizList()
    {
        return quizService.getQuizList();
    }

    @PostMapping ("/quiz") //선착순퀴즈 수정 버튼
    @Operation (summary = "선착순퀴즈 이벤트 수정버튼", description = "https://www.figma.com/design/HhnC3JbEYv2qqQaP6zdhnI?node-id=2355-4#887450213")
    public QuizDTO updateQuiz(@Validated @RequestBody QuizDTO quizDTO) {
        return quizService.updateQuiz(quizDTO);
    }

    @PostMapping("/winners")//당첨자 추첨하기 버튼
    @Operation (summary = "캐스퍼 레이싱 당첨자 추첨하기 버튼" , description = "https://www.figma.com/design/HhnC3JbEYv2qqQaP6zdhnI?node-id=2355-702#886184643")
    public ResponseEntity<String> drawWinners(@Validated @RequestBody List<WinnerSettingDTO> winnerSettingDTOList){
        int size = racingService.getEventUserSize((long)EventId.Racing.ordinal());
        int drawNum = 0;
        for(WinnerSettingDTO winnerSettingDTO : winnerSettingDTOList) {
            drawNum += winnerSettingDTO.getNum();
        }
        if(size<drawNum) {
            return new ResponseEntity<>("추첨하려는 총 인원이 참가자보다 많습니다",HttpStatus.INTERNAL_SERVER_ERROR);
        }//만약 뽑으려는 숫자가 현재 참가자보다 많을시에는
        racingService.drawWinners(winnerSettingDTOList, (long)EventId.Racing.ordinal());
        return new ResponseEntity<>("Http 200 OK",HttpStatus.OK);
    }
    @GetMapping("/winners") //당첨자 목록 버튼
    @Operation(summary = "캐스퍼 레이싱 당첨자 목록" , description = "https://www.figma.com/design/HhnC3JbEYv2qqQaP6zdhnI?node-id=2355-1024#887658590")
    public List<RacingWinnersDTO> getWinnerList() {
        return racingService.getWinnerList((long)EventId.Racing.ordinal());
    }

    @GetMapping("/personality")
    @Operation(summary = "레이싱 게임 유형검사" ,description = "https://www.figma.com/design/HhnC3JbEYv2qqQaP6zdhnI?node-id=2355-211#887801621")
    public List<PersonalityTestDTO> getPersonalities(){
        return racingService.getPersonalityList();
    }

    @PostMapping("/personality") //유형 검사 질문박스 수정
    public PersonalityTestDTO updatePersonality(@Validated @RequestBody PersonalityTestDTO personalityTestDTO){
        return racingService.updatePersonalityTest(personalityTestDTO);
    }

}
