package newCar.event_page.service;

import newCar.event_page.repository.QuizEventRepository;
import newCar.event_page.repository.RacingEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RacingEventService {

    private RacingEventRepository racingEventRepository;

    @Autowired
    public RacingEventService(RacingEventRepository racingEventRepository) {
        this.racingEventRepository=racingEventRepository;
    }
}
