package newCar.event_page.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import newCar.event_page.config.OAuthConfig;
import newCar.event_page.jwt.JwtTokenProvider;
import newCar.event_page.service.OAuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "Oauth 컨트롤러입니다", description = "카카오 로그인을 위한 컨트롤러입ㄴ다")
public class OAuthController {

    private final OAuthConfig oAuthConfig;
    private final OAuthService oAuthService;
    private final JwtTokenProvider jwtTokenProvider;

    private String redirectUrl;

    @GetMapping("/main/kakao")
    public ResponseEntity<Void> kakaoConnect(@RequestParam("redirectUrl") String redirectUrl) {
        StringBuffer url = new StringBuffer();
        this.redirectUrl=redirectUrl;
        url.append("https://kauth.kakao.com/oauth/authorize?");
        url.append("response_type=code");
        url.append("&client_id=" + oAuthConfig.getClientId());
        url.append("&redirect_uri=" + oAuthConfig.getRedirectUri());

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(url.toString()));

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<Void> kakaoCallBack(@RequestParam("code") String code){

        String kakaoAccessToken = oAuthService.getAccessToken(code);

        Map<String,String> map = oAuthService.getUserInfo(kakaoAccessToken);
        //카카오 엑세스 토큰을 이용해서 새로운 엑세스 토큰을 발급한다

        String accessToken = map.get("accessToken");
        // 리디렉션할 URL과 쿼리 파라미터 생성

        String queryParams = "accessToken=" + accessToken;

        // URL에 쿼리 파라미터 추가
        String fullRedirectUrl = this.redirectUrl+ "?" + queryParams;

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(fullRedirectUrl));

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
