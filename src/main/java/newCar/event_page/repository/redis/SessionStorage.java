package newCar.event_page.service.session;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class SessionStorage {
    private final RedisTemplate<String, Object> redisTemplate;

    public static final String SESSION_PREFIX = "session:";

    public void addSession(Session session) {
        redisTemplate.opsForValue().set(SESSION_PREFIX + session.getSessionId(), session, 30, TimeUnit.MINUTES);
    }

    public Session getSession(String sessionId) {
        return (Session) redisTemplate.opsForValue().get(SESSION_PREFIX + sessionId);
    }

    public void removeSession(String sessionId) {
        redisTemplate.delete(SESSION_PREFIX + sessionId);
    }

    public boolean isValidSession(String sessionId) {
        return redisTemplate.hasKey(SESSION_PREFIX + sessionId);
    }
}