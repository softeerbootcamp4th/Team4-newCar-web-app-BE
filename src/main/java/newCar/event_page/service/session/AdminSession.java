package newCar.event_page.service.session;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AdminSession extends Session {

    public AdminSession(String sessionId) {
        super(sessionId);
    }
}
