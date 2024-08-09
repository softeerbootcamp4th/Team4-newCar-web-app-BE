package newCar.event_page.service;

import newCar.event_page.model.dto.user.UserLightDTO;
import org.springframework.http.ResponseEntity;

public interface UserService {

    public ResponseEntity<String> login(UserLightDTO userLightDTO);
}
