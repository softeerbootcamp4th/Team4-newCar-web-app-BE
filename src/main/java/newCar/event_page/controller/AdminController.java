package newCar.event_page.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import newCar.event_page.dto.CommonEventDTO;
import newCar.event_page.dto.QuizDTO;
import newCar.event_page.dto.RacingWinnersDTO;
import newCar.event_page.dto.WinnerSettingDTO;
import newCar.event_page.eventstatus.EventStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Tag(name = "admin API" , description = "admin API 설계입니다")
@RestController
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/common-event") //이벤트 관리 버튼(이벤트 공통, 선착순 퀴즈, 캐스퍼 레이싱 설정값 불러옴)
    @Operation (summary = "이벤트명, 상태, 담당자, 진행기간", description = "https://www.figma.com/design/HhnC3JbEYv2qqQaP6zdhnI?node-id=2355-435#886120115")
    public CommonEventDTO commonEvent(){
        return new CommonEventDTO("테스트입니다" , "배진환", EventStatus.IN_PROGRESS,
                LocalDateTime.of(2024,1,31,18,30),
                LocalDateTime.of(2024,2,28,18,30));
    }


    @PostMapping("/common-event")
    @Operation (summary = "이벤트명, 상태, 담당자, 진행기간 수정", description = "https://www.figma.com/design/HhnC3JbEYv2qqQaP6zdhnI?node-id=2355-435#886180035")
    @Parameters({
            @Parameter(name = "eventName", description = "이벤트명", example = "소프티어 이벤트"),
            @Parameter(name = "status", description = "상태", example = "INPROGRESS"),
            @Parameter(name = "eventManager", description = "담당자", example = "배진환"),
            @Parameter(name = "startTime", description = "이벤트 시작 시간", example = "2024-01-31T18:30:00"),
            @Parameter(name = "endTime", description = "이벤트 종료 시간", example = "2024-02-28T18:30:00")
    })
    public CommonEventDTO setEvent(@ModelAttribute CommonEventDTO CommonEventDTO){
        return CommonEventDTO;
    }


    @GetMapping("/quiz")
    @Operation( summary = "선착순 퀴즈 이벤트 정보", description= "https://www.figma.com/design/HhnC3JbEYv2qqQaP6zdhnI?node-id=2355-4#887413777")
    public List<QuizDTO> getQuiz()
    {
        List<QuizDTO> quizDTOList = new ArrayList<>();
        QuizDTO temp1 = new QuizDTO(1L,100, LocalDate.of(2024,1,31),"첫번째 질문",
                "보기1","보기2","보기3","보기4",4);
        QuizDTO temp2 = new QuizDTO(2L,50, LocalDate.of(2024,3,31),"두번째 질문",
                "보기1","보기2","보기3","보기4",1);
        quizDTOList.add(temp1);
        quizDTOList.add(temp2);
        return quizDTOList;
    }

    @PostMapping ("/quiz") //선착순퀴즈 수정 버튼
    @Operation (summary = "선착순퀴즈 이벤트 수정버튼", description = "https://www.figma.com/design/HhnC3JbEYv2qqQaP6zdhnI?node-id=2355-4#887450213")
    public QuizDTO quizEdit(@ModelAttribute QuizDTO quizDTO) {
        return quizDTO;
    }

    @PostMapping("/winners")//당첨자 추첨하기 버튼
    @Operation (summary = "캐스퍼 레이싱 당첨자 추첨하기 버튼" , description = "https://www.figma.com/design/HhnC3JbEYv2qqQaP6zdhnI?node-id=2355-702#886184643")
    public void selectWinner(@RequestBody List<WinnerSettingDTO> winnerSettingDTOList){


    }

    @GetMapping("/winners") //당첨자 목록 버튼
    @Operation(summary = "캐스퍼 레이싱 당첨자 목록" , description = "https://www.figma.com/design/HhnC3JbEYv2qqQaP6zdhnI?node-id=2355-1024#887658590")
    public List<RacingWinnersDTO> listWinners() {
        RacingWinnersDTO temp1= new RacingWinnersDTO("배진환","010-5239-0966",LocalDate.of(2024,07,22));
        RacingWinnersDTO temp2= new RacingWinnersDTO("장준하","010-1234-5678",LocalDate.of(2024,06,22));
        List<RacingWinnersDTO> list = new ArrayList<>();
        list.add(temp1);
        list.add(temp2);
        System.out.println(temp1.toString());
        return list;
    }


    @PostMapping("/racing") //유형 검사 질문박스 수정
    public void editRacing(@RequestBody HashMap<String, Object> map){

    }




}
