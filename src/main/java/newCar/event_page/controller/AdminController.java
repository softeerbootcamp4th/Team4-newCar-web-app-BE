package newCar.event_page.controller;


import newCar.event_page.service.QuizEventService;
import newCar.event_page.service.RacingEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;


@RestController
@RequestMapping("/admin")
public class AdminController {

    private RacingEventService racingEventService;

    private QuizEventService quizEventService;

    @Autowired
    public AdminController(RacingEventService racingEventService, QuizEventService quizEventService) {
        this.racingEventService=racingEventService;
        this.quizEventService=quizEventService;
    }

    @GetMapping("/manageEvent") //이벤트 관리 버튼(이벤트 공통, 선착순 퀴즈, 캐스퍼 레이싱 설정값 불러옴)
    public void manageEvent(@RequestBody HashMap<String, Object> map){

    }

    @PostMapping("/setEvent")//이벤트 공통 섹션에서 이벤트명, 담당자, 상태, 진행 기간 설정
    public void setEvent(@RequestBody HashMap<String,Object> map) {

    }

    @PostMapping ("/editQuiz") //선착순퀴즈 수정 버튼
    public void quizEdit(@RequestBody HashMap<String, Object> map) {

    }

    @PostMapping("/editRacing") //유형 검사 질문박스 수정
    public void editRacing(@RequestBody HashMap<String, Object> map){

    }

    @GetMapping("/getWinnerSetting") // 당첨자 관리 탭의 추첨하기 버튼을 누르면 서버에서 이전에 입력했떤(등수 - 명수) 데이터를 보내준다
    public void getWinnerSetting(@RequestBody HashMap<String,Object> map){

    }

    @PostMapping("/selectWinner")//당첨자 추첨하기 버튼
    public void selectWinner(@RequestBody HashMap<String,Object> map){

    }


    @GetMapping("/listWinners") //당첨자 목록 버튼
    public void listWinners(@RequestBody HashMap<String,Object> map) {

    }







}
