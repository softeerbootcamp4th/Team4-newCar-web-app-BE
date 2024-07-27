package newCar.event_page.service;

import newCar.event_page.repository.RacingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RacingService {


    private final RacingRepo racingRepo;

    @Autowired
    public RacingService(RacingRepo racingRepo)
    {
        this.racingRepo=racingRepo;
    }
}
