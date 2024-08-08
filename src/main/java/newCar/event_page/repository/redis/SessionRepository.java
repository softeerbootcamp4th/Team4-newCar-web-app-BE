package newCar.event_page.repository.redis;

import jakarta.validation.constraints.NotEmpty;
import lombok.NonNull;
import newCar.event_page.model.session.Session;

import java.util.Optional;

public interface SessionRepository {

    public void save(@NonNull Session session);

    public void saveAll(Iterable<Session> sessionIterable);

    public Optional<Session> findById(@NotEmpty String sessionId);

    public boolean existsById(@NotEmpty String sessionId);

    public void deleteById(@NotEmpty String sessionId);

    public void deleteAllById(Iterable<String> sessionIds);
}
