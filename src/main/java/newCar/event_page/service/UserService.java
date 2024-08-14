package newCar.event_page.service;

import newCar.event_page.model.dto.user.*;
import newCar.event_page.model.enums.UserQuizStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface UserService {

    public ResponseEntity<Map<String, String>> login(UserLightDTO userLightDTO);

    public ResponseEntity<UserQuizDTO> getQuiz(Long quizEventId);

    public ResponseEntity<UserEventTimeDTO> getEventTime();

    public ResponseEntity<List<UserPersonalityTestDTO>> getPersonalityTestList();

    public ResponseEntity<Map<String, Object>> submitPersonalityTest(List<UserPersonalityAnswerDTO> userPersonalityAnswerDTOList,
                                                                     String authorizationHeader);

    public ResponseEntity<Map<String,UserQuizStatus>> submitQuiz(UserQuizAnswerDTO answer, String authorizationHeader);

    public ResponseEntity<Map<String,String>> dummyToken();

    public Map<String,String> kakaoLogin(Map<String,String> userInfo);
}
