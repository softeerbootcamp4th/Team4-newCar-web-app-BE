package newCar.event_page.service;

import lombok.RequiredArgsConstructor;
import newCar.event_page.dto.PersonalityTestDTO;
import newCar.event_page.dto.RacingWinnersDTO;
import newCar.event_page.dto.WinnerSettingDTO;
import newCar.event_page.entity.event.EventUser;
import newCar.event_page.entity.event.racing.PersonalityTest;
import newCar.event_page.entity.event.racing.RacingWinner;
import newCar.event_page.participant.Participant;
import newCar.event_page.repository.EventUserRepository;
import newCar.event_page.repository.racing.PersonalityTestRepository;
import newCar.event_page.repository.racing.RacingEventRepository;
import newCar.event_page.repository.racing.RacingWinnerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Transactional
@Service
public class RacingService {

    private final PersonalityTestRepository personalityTestRepository;
    private final EventUserRepository eventUserRepository;
    private final RacingWinnerRepository racingWinnerRepository;
    private final RacingEventRepository racingEventRepository;

    private double totalWeight;

    public List<PersonalityTestDTO> getPersonalityList() {
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


    public void drawWinners(List<WinnerSettingDTO> winnerSettingDTOList, Long eventId) {
        racingWinnerRepository.deleteByEventId(eventId);//deleteById
        List<EventUser> eventUserList = eventUserRepository.findByEventId(eventId); //Racing게임을 참가한 사람들의 목록을 받아온다
        Set<Participant> participantSet = new LinkedHashSet<>();
        totalWeight = 0;
        double weight = 0;
        for (EventUser eventUser : eventUserList) {
            weight = getWeight(eventUser.getUser().getClickNumber());
            participantSet.add(new Participant(eventUser.getUser().getId(), weight));
            totalWeight += weight;
        }//각 참가자의 가중치와 전체 가중치를 구해 준다
        setWinners(winnerSettingDTOList, eventId, participantSet);
    }



    public int getEventUserSize(Long eventId) {
        return eventUserRepository.findByEventId(eventId).size();
    }

    public List<RacingWinnersDTO> getWinnerList(Long eventId) {
        List<RacingWinner> racingWinnerList = racingWinnerRepository.findByEventId(eventId);
        List<RacingWinnersDTO> racingWinnersDTOList = new ArrayList<>();
        for(RacingWinner racingWinner : racingWinnerList) {
            racingWinnersDTOList.add(RacingWinnersDTO.toDTO(racingWinner));
        }
        return racingWinnersDTOList;
    }

    private void setWinners(List<WinnerSettingDTO> winnerSettingDTOList, Long eventId, Set<Participant> participantSet) {
        for (WinnerSettingDTO winnerSettingDTO : winnerSettingDTOList) {
            int numberOfWinners = winnerSettingDTO.getNum();// 각 등수별로 몇명 뽑는지 넘어온 설정 값으로 참조
            int rank = winnerSettingDTO.getRank(); //이번 추첨은 어떤 랭크인지
            for (int i = 0; i < numberOfWinners; i++) {
                Participant winner = drawOneWinner(participantSet,totalWeight);
                totalWeight -= winner.weight; //전체가중치에서 당첨자의 가중치를 빼준다
                participantSet.remove(winner);//중복 제거를 위해 Set에서 제거 해준다
                racingWinnerRepository.save(new RacingWinner(racingEventRepository.getReferenceById(eventId),
                        eventUserRepository.findByUserIdAndEvendId(winner.userId, eventId),rank));
            }
        }
    }

    private Participant drawOneWinner(Set<Participant> participantSet,double totalWeight){
        Random rand = new Random();
        int maxInt = (int)totalWeight; //totalWeight가 8.45일 경우 8이 저장됨
        int randomInt = rand.nextInt(maxInt) + 1; // 1~8이 저장됨
        if (randomInt == maxInt) { //만약 randomInt가 최대값으로 들어왔다면 마지막이 당첨자이다
            Participant participant = findLast(participantSet);//참가자리스트의 마지막
            return participant;
        }
        double cumulativeWeight = 0; // 누적 가중치
        double randomValue = randomInt + rand.nextDouble(); //랜덤 값
        for (Participant participant : participantSet) {
            cumulativeWeight += participant.weight;
            if (randomValue <= cumulativeWeight) {
                return participant;
            }
        }
        return null;
    }

    private Participant findLast(Set<Participant> participantSet){
        Participant lastElement = null;
        //위에서 선언한 set변수
        for (Participant participant : participantSet) {
            lastElement = participant;
        }
        return lastElement;
    }

    private double getWeight(int clickNumber) {
        return 1 + ((Math.log(clickNumber+1))/(Math.log(30)));
    }
}

