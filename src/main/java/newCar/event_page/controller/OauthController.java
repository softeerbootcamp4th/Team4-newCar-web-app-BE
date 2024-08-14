package newCar.event_page.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import newCar.event_page.config.OauthConfig;
import newCar.event_page.jwt.JwtTokenProvider;
import newCar.event_page.service.OauthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@Tag(name = "Oauth 컨트롤러입니다", description = "카카오 로그인을 위한 컨트롤러입ㄴ다")
public class OauthController {

    private final OauthConfig oauthConfig;
    private final OauthService oauthService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/main/kakao")
    public ResponseEntity<Void> kakaoConnect() {
        StringBuffer url = new StringBuffer();
        url.append("https://kauth.kakao.com/oauth/authorize?");
        url.append("response_type=code");
        url.append("&client_id=" + oauthConfig.getClientId());
        //url.append("&redirect_uri=" + oauthConfig.getRedirectUri());
        url.append("&redirect_uri=" + "http://localhost:8080/kakao/callback");

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(url.toString()));

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<Void> kakaoCallBack(@RequestParam("code") String code){

        String accessToken = oauthService.getAccessToken(code);
        System.out.println("accessToken" + accessToken);
        HashMap<String,Object> map = oauthService.getUserInfo(accessToken);

        HttpHeaders headers = new HttpHeaders();

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

}
