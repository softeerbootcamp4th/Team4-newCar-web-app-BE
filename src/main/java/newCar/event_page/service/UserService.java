package newCar.event_page.service;

import newCar.event_page.model.dto.user.UserEventTimeDTO;
import newCar.event_page.model.dto.user.UserLightDTO;
import newCar.event_page.model.dto.user.UserPersonalityTestDTO;
import newCar.event_page.model.dto.user.UserQuizDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    public ResponseEntity<String> login(UserLightDTO userLightDTO);

    public UserQuizDTO getQuiz(Long quizEventId);

    public UserEventTimeDTO getEventTime();

    public List<UserPersonalityTestDTO> getPersonalityTestList();
}
