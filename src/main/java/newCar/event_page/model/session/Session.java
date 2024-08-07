package newCar.event_page.service.session;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class Session implements Serializable {
    private String sessionId;
}
