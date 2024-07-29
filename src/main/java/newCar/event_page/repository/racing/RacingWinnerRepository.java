package newCar.event_page.repository.racing;

import newCar.event_page.entity.event.racing.RacingWinner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RacingWinnerRepository extends JpaRepository<RacingWinner,Long> {
}
