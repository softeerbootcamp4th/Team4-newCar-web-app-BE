package newCar.event_page.service;

import newCar.event_page.model.dto.admin.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface AdminService {

    public ResponseEntity<AdminEventCommonDTO> getCommonEventDetails();

    public ResponseEntity<AdminEventCommonDTO> updateCommonEventDetails(AdminEventCommonDTO eventCommonDTO);

    public ResponseEntity<List<AdminQuizDTO>> getQuizList(Long quizEventId);

    public ResponseEntity<AdminQuizDTO> updateQuiz(AdminQuizDTO adminQuizDTO);

    public ResponseEntity<String> drawRacingWinners(List<AdminWinnerSettingDTO> winnerSettingDTOList, Long racingEventId);

    public ResponseEntity<List<AdminRacingWinnersDTO>> getRacingWinnerList(Long racingEventId);

    public ResponseEntity<List<AdminPersonalityTestDTO>> getPersonalityTestList();

    public ResponseEntity<AdminPersonalityTestDTO> updatePersonalityTest(AdminPersonalityTestDTO personalityTestDTO);

    public ResponseEntity<Map<String,String>> login(AdminLoginDTO adminLoginDTO);

}
