package newCar.event_page.controller;


import newCar.event_page.model.RacingEvent;
import newCar.event_page.service.QuizEventService;
import newCar.event_page.service.RacingEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class RequestController {

    private RacingEventService racingEventService;

    private QuizEventService quizEventService;

    @Autowired
    public RequestController(RacingEventService racingEventService, QuizEventService quizEventService) {
        this.racingEventService=racingEventService;
        this.quizEventService=quizEventService;
    }
}
