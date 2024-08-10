package newCar.event_page.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import newCar.event_page.model.dto.user.UserLightDTO;
import newCar.event_page.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Tag(name = "Main API", description = "Main API 설계입니다")
@RestController
@RequestMapping("/main")
@CrossOrigin("*")
public class MainController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> userLogin(@Valid @RequestBody UserLightDTO userLightDTO) {
        return userService.login(userLightDTO);
    }
}
