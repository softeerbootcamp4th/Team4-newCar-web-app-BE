package newCar.event_page.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface OAuthService {

    public String getAccessToken(String code);

    public Map<String,String> getUserInfo(String kakaoAccessToken);
}
