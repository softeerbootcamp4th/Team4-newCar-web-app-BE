package newCar.event_page.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import newCar.event_page.config.OauthConfig;
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
public class OauthServiceImpl implements OauthService{

    private final OauthConfig oauthConfig;



    @Override
    public String getAccessToken(String code){
        String accessToken = "";
        String reqUrl = "https://kauth.kakao.com/oauth/token";

        try{
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //필수 헤더 세팅
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
            conn.setDoOutput(true); //OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();

            //필수 쿼리 파라미터 세팅
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=").append(oauthConfig.getClientId());
            sb.append("&client_secret=").append(oauthConfig.getClientSecretId());
            sb.append("&redirect_uri=").append("http://localhost:8080/kakao/callback");
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

    @Override
    public HashMap<String,Object> getUserInfo(String accessToken){

        HashMap<String, Object> userInfo = new HashMap<>();
        String reqUrl = "https://kapi.kakao.com/v2/user/me";
        try{
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");//GET 이든 POST이든 상관없는듯
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

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
            System.out.println(result);

            JsonElement element = JsonParser.parseString(result);

            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

            String nickname = properties.getAsJsonObject().get("nickname").getAsString();
            //String email = kakaoAccount.getAsJsonObject().get("email").getAsString();

            userInfo.put("nickname", nickname);
            //userInfo.put("email", email);

            System.out.println("nickname = "+nickname);
            //System.out.println("email = " + email);

            br.close();

        }catch (Exception e){
            e.printStackTrace();
        }

        return userInfo;
    }

}
