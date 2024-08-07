package newCar.event_page.model.session;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserSession extends Session {
    private Long userId;

    public UserSession(String sessionId) {
        super(sessionId);
    }
}
