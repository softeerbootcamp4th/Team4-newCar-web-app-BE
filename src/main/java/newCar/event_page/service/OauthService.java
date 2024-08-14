package newCar.event_page.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public interface OauthService {

    public String getAccessToken(String code);

    public Map<String,String> getUserInfo(String kakaoAccessToken);
}
