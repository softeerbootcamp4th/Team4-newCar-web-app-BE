package newCar.event_page.service;

import jakarta.servlet.http.HttpServletRequest;
import newCar.event_page.model.dto.user.*;
import newCar.event_page.model.enums.UserQuizStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.Map;

public interface UserService {

    public ResponseEntity<Map<String, String>> login(UserLightDTO userLightDTO);

    public ResponseEntity<UserQuizDTO> getQuiz(Long quizEventId);

    public ResponseEntity<UserEventTimeDTO> getEventTime();

    public ResponseEntity<List<UserPersonalityTestDTO>> getPersonalityTestList();

    public ResponseEntity<UserPersonalityUrlDTO> submitPersonalityTest(List<UserPersonalityAnswerDTO> userPersonalityAnswerDTOList,
                                                                     String authorizationHeader);

    public ResponseEntity<Map<String, UserQuizStatus>> submitQuiz(UserQuizAnswerDTO answer, String authorizationHeader);

    public ResponseEntity<Map<String, String>> dummyToken();

    public Map<String, String> kakaoLogin(UserKakaoInfoDTO userKakaoInfoDTO);

    public ResponseEntity<UserClickNumberDTO> getClickNumber(String authorizationHeader);

    public ResponseEntity<Void> plusClickNumber(String url, HttpServletRequest request);

    public ResponseEntity<UserInfoDTO> getUserInfo(String authorizationHeader);
}
