package newCar.event_page.service;

import newCar.event_page.dto.PersonalityTestDTO;
import newCar.event_page.entity.event.racing.PersonalityTest;
import newCar.event_page.repository.PersonalityTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class RacingService {

    private final PersonalityTestRepository personalityTestRepository;

    @Autowired
    public RacingService(PersonalityTestRepository personalityTestRepository)
    {
        this.personalityTestRepository=personalityTestRepository;
    }

    public List<PersonalityTestDTO> getList()
    {
        List<PersonalityTest> list = personalityTestRepository.findAll();
        List<PersonalityTestDTO> personalityTestDTOList = new ArrayList<>();
        for(PersonalityTest temp : list)
        {
            personalityTestDTOList.add(PersonalityTestDTO.toDTO(temp));
        }
        return personalityTestDTOList;
    }

    public PersonalityTestDTO updatePersonalityTest(PersonalityTestDTO personalityTestDTO)
    {
        PersonalityTest personalityTest = personalityTestRepository.findById(personalityTestDTO.getId())
                .orElseThrow(()->new NoSuchElementException("PersonalityTest Not Found"));
        personalityTest.updatePersonality(personalityTestDTO);
        personalityTestRepository.save(personalityTest);
        return PersonalityTestDTO.toDTO(personalityTest);
    }
}
