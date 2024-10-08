package newCar.event_page.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import newCar.event_page.config.OAuthConfig;
import newCar.event_page.model.dto.user.UserKakaoInfoDTO;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuthServiceImpl implements OAuthService {

    private final OAuthConfig oauthConfig;
    private final UserService userService;

    private final String CONTENT_TYPE = "application/x-www-form-urlencoded;charset=utf-8";

    @Override
    public String getAccessToken(String code){
        String accessToken = "";

        try{
            String TOKEN_URL = "https://kauth.kakao.com/oauth/token";
            URL url = new URL(TOKEN_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //필수 헤더 세팅
            conn.setRequestProperty("Content-type", CONTENT_TYPE);
            conn.setDoOutput(true); //OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();

            //필수 쿼리 파라미터 세팅
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=").append(oauthConfig.getClientId());
            sb.append("&client_secret=").append(oauthConfig.getClientSecretId());
            sb.append("&redirect_uri=").append(oauthConfig.getRedirectUri());
            sb.append("&code=").append(code);

            bw.write(sb.toString());
            bw.flush();//여기서 요청을 보내준다

            int responseCode = conn.getResponseCode();

            BufferedReader br;
            if (responseCode >= 200 && responseCode < 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            String line = "";
            StringBuilder responseSb = new StringBuilder();
            while((line = br.readLine()) != null){
                responseSb.append(line);
            }
            String result = responseSb.toString();

            JsonElement element = JsonParser.parseString(result);
            accessToken = element.getAsJsonObject().get("access_token").getAsString();

            br.close();
            bw.close();

        }catch (Exception e){
            e.printStackTrace();
        }

        return accessToken;
    }


    //카카오 엑세스 토큰을 통해 userInfo를 받아온다
    @Override
    public Map<String, String> getUserInfo(String kakaoAccessToken){

        UserKakaoInfoDTO userKakaoInfoDTO = new UserKakaoInfoDTO();
        try{
            String USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";
            URL url = new URL(USER_INFO_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");//GET 이든 POST이든 상관없는듯
            conn.setRequestProperty("Authorization", "Bearer " + kakaoAccessToken);
            conn.setRequestProperty("Content-type", CONTENT_TYPE);

            int responseCode = conn.getResponseCode();

            BufferedReader br;
            if (responseCode >= 200 && responseCode <= 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            String line = "";
            StringBuilder responseSb = new StringBuilder();
            while((line = br.readLine()) != null){
                responseSb.append(line);
            }
            String result = responseSb.toString();

            JsonElement element = JsonParser.parseString(result);

            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

            userKakaoInfoDTO.setNickname(properties.getAsJsonObject().get("nickname").getAsString());
            userKakaoInfoDTO.setEmail(kakaoAccount.getAsJsonObject().get("email").getAsString());
        }catch (Exception e){
            e.printStackTrace();
        }

        return userService.kakaoLogin(userKakaoInfoDTO);
    }

}
