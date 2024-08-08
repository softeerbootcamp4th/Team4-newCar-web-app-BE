package newCar.event_page.repository.redis;

import jakarta.validation.constraints.NotEmpty;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import newCar.event_page.model.session.Session;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisSessionStore implements SessionRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    public static final String SESSION_PREFIX = "session:";

    @Override
    public void save(@NonNull Session session) {
        redisTemplate.opsForValue().set(SESSION_PREFIX + session.getSessionId(), session, 30, TimeUnit.MINUTES);
    }

    @Override
    public void saveAll(Iterable<Session> sessionIterable) {
        for (Session entity : sessionIterable) {
            save(entity);
        }
    }

    @Override
    public Optional<Session> findById(@NotEmpty String sessionId) {
        Session session = (Session) redisTemplate.opsForValue().get(SESSION_PREFIX + sessionId);
        return Optional.ofNullable(session);
    }

    @Override
    public boolean existsById(@NotEmpty String sessionId) {
        //Redis 서버와의 연결이 일시적으로 끊어지거나 문제가 있을 경우, hasKey 메서드가 null을 반환할 수 있다.
        return Boolean.TRUE.equals(redisTemplate.hasKey(SESSION_PREFIX + sessionId));
    }

    @Override
    public void deleteById(@NotEmpty String sessionId) {
        redisTemplate.delete(SESSION_PREFIX + sessionId);
    }

    @Override
    public void deleteAllById(Iterable<String> sessionIds) {
        for (String sessionId : sessionIds) {
            deleteById(sessionId);
        }
    }
}