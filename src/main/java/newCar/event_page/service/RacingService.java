package newCar.event_page.service;

import newCar.event_page.dto.PersonalityTestDTO;
import newCar.event_page.dto.WinnerSettingDTO;
import newCar.event_page.entity.event.EventUser;
import newCar.event_page.entity.event.racing.PersonalityTest;
import newCar.event_page.entity.event.racing.RacingWinner;
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
        List<EventUser> list = eventUserRepository.findByEventId(1L); //Racing게임을 참가한 사람들의 목록을 받아온다
        List<Participant> participantList = new ArrayList<>();
        double totalWeight = 0;
        double weight = 0;
        Random rand = new Random();
        for(EventUser eventUser : list) {
            weight = getWeight(eventUser.getUser().getClickNumber());
            participantList.add(new Participant(eventUser.getUser().getId(), weight));
            totalWeight +=weight;
        }
        for(WinnerSettingDTO winnerSettingDTO : winnerSettingDTOList) {
            int numberOfWinners = winnerSettingDTO.getNum();// 각 등수별로 몇명 뽑는지 넘어온 설정 값으로 참조
            int rank = winnerSettingDTO.getRank(); //이번 추첨은 어떤 랭크인지
            for(int i = 0 ; i<numberOfWinners ; i++) {
                int maxInt = (int)totalWeight; //totalWeight가 8.45일 경우 8이 저장됨
                int randomInt = rand.nextInt((int)totalWeight)+1; // 1~8이 저장됨
                RacingWinner racingWinner = new RacingWinner(); // 당첨자 엔티티
                if(randomInt == maxInt){ //만약 randomInt가 최대값으로 들어왔다면 마지막이 당첨자이다
                    Participant winner = participantList.get(participantList.size()-1);//참가자리스트의 마지막
                    participantList.remove(winner);//중복 제거를 위해 참가자 리스트에서 제외시킨다
                    totalWeight -= winner.weight; // 전체 가중치 감소
                    racingWinner.setRank(rank);
                    racingWinner.setEventUser(eventUserRepository.findByUserId(winner.userId));
                    racingWinner.setRacingEvent();
                    racingWinnerRepository.save(racingWinner);
                    continue;
                }
                double cumulativeWeight = 0; // 누적 가중치
                double randomValue = randomInt + rand.nextDouble(); //랜덤 값
                for(Participant participant : participantList) {
                    cumulativeWeight += participant.weight;
                    if(randomValue <=cumulativeWeight) {
                        participantList.remove(participant); // 중복 제거
                        totalWeight -= participant.weight; //전체 가중치 감소
                        racingWinner.setRank(rank);
                        racingWinner.setEventUser(eventUserRepository.findByUserId(participant.userId));
                        racingWinner.setRacingEvent();
                        racingWinnerRepository.save(racingWinner);
                        break;
                    }
                }
            }
        }
    }


    private double getWeight(int clickNumber) {
        return 1 + (Math.log(clickNumber+1)/Math.log(30));
    }
}

class Participant {
    public Long userId;
    public double weight;
    public Participant(Long userId, double weight) {
        this.userId=userId;
        this.weight-=weight;
    }
}
