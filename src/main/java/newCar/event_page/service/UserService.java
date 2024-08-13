package newCar.event_page.service;

import newCar.event_page.model.dto.user.*;
import newCar.event_page.model.entity.Team;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface UserService {

    public ResponseEntity<Map<String, String>> login(UserLightDTO userLightDTO);

    public UserQuizDTO getQuiz(Long quizEventId);

    public UserEventTimeDTO getEventTime();

    public List<UserPersonalityTestDTO> getPersonalityTestList();


    public ResponseEntity<Map<String, Object>> personalityTest(List<UserPersonalityAnswerDTO> userPersonalityAnswerDTOList,
                                                               String authorizationHeader);

}
