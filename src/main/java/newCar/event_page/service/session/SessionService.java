package newCar.event_page.service;

import lombok.RequiredArgsConstructor;
import newCar.event_page.service.session.SessionStorage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionStorage sessionStorage;

    public boolean validateSession(String sessionId) {
        return sessionStorage.isValidSession(sessionId);
    }
}