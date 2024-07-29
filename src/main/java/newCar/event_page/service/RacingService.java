package newCar.event_page.service;

import newCar.event_page.dto.PersonalityTestDTO;
import newCar.event_page.dto.WinnerSettingDTO;
import newCar.event_page.entity.event.EventUser;
import newCar.event_page.entity.event.racing.PersonalityTest;
import newCar.event_page.repository.EventUserRepository;
import newCar.event_page.repository.racing.PersonalityTestRepository;
import newCar.event_page.repository.racing.RacingWinnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class RacingService {

    private final PersonalityTestRepository personalityTestRepository;
    private final EventUserRepository eventUserRepository;
    private final RacingWinnerRepository racingWinnerRepository;

    @Autowired
    public RacingService(PersonalityTestRepository personalityTestRepository, EventUserRepository eventUserRepository
            ,RacingWinnerRepository racingWinnerRepository) {
        this.personalityTestRepository=personalityTestRepository;
        this.eventUserRepository=eventUserRepository;
        this.racingWinnerRepository=racingWinnerRepository;
    }

    public List<PersonalityTestDTO> getList() {
        List<PersonalityTest> list = personalityTestRepository.findAll();
        List<PersonalityTestDTO> personalityTestDTOList = new ArrayList<>();
        for(PersonalityTest temp : list) {
            personalityTestDTOList.add(PersonalityTestDTO.toDTO(temp));
        }
        return personalityTestDTOList;
    }

    public PersonalityTestDTO updatePersonalityTest(PersonalityTestDTO personalityTestDTO) {
        PersonalityTest personalityTest = personalityTestRepository.findById(personalityTestDTO.getId()).get();
        personalityTest.update(personalityTestDTO);
        personalityTestRepository.save(personalityTest);
        return PersonalityTestDTO.toDTO(personalityTest);
    }

    public void drawWinners(List<WinnerSettingDTO> winnerSettingDTOList) {
        List<EventUser> list = eventUserRepository.findByEventId();
        List<Participant> participantList = new ArrayList<>();
        for(EventUser eventUser : list) {
            participantList.add(new Participant(eventUser.getUser().getId(), eventUser.getUser().getClickNumber()));
        }
    }

    private double getWeight(int clickNumber)
    {
        return 1 + (Math.log(clickNumber+1)/Math.log(30));
    }
}
class Participant
{
    public Long userId;
    public double weight;
    public Participant(Long userId, double weight) {
        this.userId=userId;
        this.weight-=weight;
    }
}
