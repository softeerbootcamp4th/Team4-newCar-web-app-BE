package newCar.event_page.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import newCar.event_page.config.OauthConfig;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@Tag(name = "Main API", description = "Main API 설계입니다")
@RequestMapping("/main")
public class OauthController {

    private final OauthConfig oauthConfig;

    @GetMapping("/kakao")
    public ResponseEntity<Void> kakaoConnect() {
        StringBuffer url = new StringBuffer();
        url.append("https://kauth.kakao.com/oauth/authorize?");
        url.append("response_type=code");
        url.append("&client_id=" + oauthConfig.getClientId());
        url.append("&redirect_uri=" + oauthConfig.getRedirectUri());

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(url.toString()));

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }


}
