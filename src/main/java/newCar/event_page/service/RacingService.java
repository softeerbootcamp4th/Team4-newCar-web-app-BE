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
import java.util.Random;

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
        double totalWeight = 0;
        double weight = 0;
        Random rand = new Random();
        System.out.println(rand.nextDouble());
        for(EventUser eventUser : list) {
            weight = getWeight(eventUser.getUser().getClickNumber());
            participantList.add(new Participant(eventUser.getUser().getId(), weight));
            totalWeight +=weight;
        }
        for(WinnerSettingDTO winnerSettingDTO : winnerSettingDTOList) {
            int numberOfWinners = winnerSettingDTO.getNum();// 각 등수별로 몇명 뽑는지 넘어온 설정 값으로 참조
            Long rank = winnerSettingDTO.getId();
            for(int i = 0 ; i<numberOfWinners ; i++) {
                double randomValue = rand.nextInt((int)totalWeight)+1 +rand.nextDouble(); //totalWeight가 8.45 경우


            }
        }

    }

    private double getWeight(int clickNumber) {
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
