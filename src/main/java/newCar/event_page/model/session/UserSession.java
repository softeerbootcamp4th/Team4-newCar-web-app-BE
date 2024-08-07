package newCar.event_page.service.session;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserSession extends Session {
    private Long userId;

    public UserSession(String sessionId) {
        super(sessionId);
    }
}
