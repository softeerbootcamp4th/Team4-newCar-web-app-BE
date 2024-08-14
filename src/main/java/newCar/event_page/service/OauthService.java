package newCar.event_page.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public interface OauthService {

    public String getAccessToken(String code);

    public HashMap<String,Object> getUserInfo(String accessToken);
}
