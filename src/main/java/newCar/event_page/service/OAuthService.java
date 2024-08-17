package newCar.event_page.service;

import java.util.Map;

public interface OAuthService {

    public String getAccessToken(String code);

    public Map<String,String> getUserInfo(String kakaoAccessToken);
}
