package newCar.event_page.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import newCar.event_page.dto.*;
import newCar.event_page.service.EventService;
import newCar.event_page.service.QuizService;
import newCar.event_page.service.RacingService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void drawWinners(@RequestBody List<WinnerSettingDTO> winnerSettingDTOList){
        racingService.drawWinners(winnerSettingDTOList);
    }

    @GetMapping("/winners") //당첨자 목록 버튼
    @Operation(summary = "캐스퍼 레이싱 당첨자 목록" , description = "https://www.figma.com/design/HhnC3JbEYv2qqQaP6zdhnI?node-id=2355-1024#887658590")
    public List<RacingWinnersDTO> getyWinnersList() {
        RacingWinnersDTO temp1= new RacingWinnersDTO("배진환","010-5239-0966",LocalDate.of(2024,07,22));
        RacingWinnersDTO temp2= new RacingWinnersDTO("장준하","010-1234-5678",LocalDate.of(2024,06,22));
        List<RacingWinnersDTO> list = new ArrayList<>();
        list.add(temp1);
        list.add(temp2);
        System.out.println(temp1.toString());
        return list;
    }

    @GetMapping("/personality")
    @Operation(summary = "레이싱 게임 유형검사" ,description = "https://www.figma.com/design/HhnC3JbEYv2qqQaP6zdhnI?node-id=2355-211#887801621")
    public List<PersonalityTestDTO> getPersonalities(){
        return racingService.getList();
    }

    @PostMapping("/personality") //유형 검사 질문박스 수정
    public PersonalityTestDTO updatePersonality(@Validated @RequestBody PersonalityTestDTO personalityTestDTO){
        return racingService.updatePersonalityTest(personalityTestDTO);
    }

}
