package newCar.event_page.service.session;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionStorage sessionStorage;

    public boolean validateSession(String sessionId) {
        return sessionStorage.isValidSession(sessionId);
    }
}