package newCar.event_page.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import newCar.event_page.config.OAuthConfig;
import newCar.event_page.model.dto.user.UserKakaoInfoDTO;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class OAuthServiceImplTest {

    @Mock
    private OAuthConfig oauthConfig;

    @Mock
    private UserService userService;

    @InjectMocks
    private OAuthServiceImpl oAuthServiceImpl;

    @Mock
    private HttpURLConnection connection;

    // 전역으로 MockedStatic 선언
    private static MockedStatic<JsonParser> mockedJsonParser;
    private static JsonElement jsonElement;
    private static JsonObject jsonObject;
    private static JsonObject properties;
    private static JsonObject kakaoAccount;

    @BeforeAll
    public static void beforeClass(){
        mockedJsonParser = mockStatic(JsonParser.class);
        jsonElement = mock(JsonElement.class);
        jsonObject = mock(JsonObject.class);
        properties = mock(JsonObject.class);
        kakaoAccount = mock(JsonObject.class);
        // Static 메서드 모킹 설정
        mockedJsonParser.when(() -> JsonParser.parseString(anyString())).thenReturn(jsonElement);
        when(jsonElement.getAsJsonObject()).thenReturn(jsonObject);
        when(jsonObject.get(any(String.class))).thenReturn(jsonElement);
        when(jsonObject.getAsJsonObject("properties")).thenReturn(properties);
        when(jsonObject.getAsJsonObject("kakao_account")).thenReturn(kakaoAccount);

        when(properties.get("nickname")).thenReturn(mock(JsonElement.class));
        when(kakaoAccount.get("email")).thenReturn(mock(JsonElement.class));

        when(properties.get("nickname").getAsString()).thenReturn("test_nickname");
        when(kakaoAccount.get("email").getAsString()).thenReturn("test_email@example.com");
    }

    @AfterAll
    public static void afterClass(){
        mockedJsonParser.close();
    }//Mockito의 mockStatic은 메모리 사용량이 높아질 수 있으므로, 반드시 필요한 경우에만 사용하고 테스트가 끝나면 자원을 해제하는 것이 중요하다

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("카카오 엑세스 토큰 받아오기_성공")
    void getAccessToken_success() throws Exception {
        String code = "test_code";
        String accessTokenResponse = "{\"access_token\": \"test_access_token\"}";
        URL url = mock(URL.class);

        // Mock OAuthConfig
        when(oauthConfig.getClientId()).thenReturn("client_id");
        when(oauthConfig.getClientSecretId()).thenReturn("client_secret");
        when(oauthConfig.getRedirectUri()).thenReturn("redirect_uri");

        // Mock HttpURLConnection behavior
        when(url.openConnection()).thenReturn(connection);
        when(connection.getResponseCode()).thenReturn(200);
        when(connection.getInputStream()).thenReturn(new ByteArrayInputStream(accessTokenResponse.getBytes()));
        when(jsonElement.getAsString()).thenReturn("test_access_token");

        String accessToken = oAuthServiceImpl.getAccessToken(code);

        assertThat(accessToken).isEqualTo("test_access_token");
    }

    @Test
    @DisplayName("카카오 엑세스 토큰으로 유저정보 받아오기")
    void getUserInfo_success() throws Exception {
        String kakaoAccessToken = "test_access_token";
        String userInfoResponse = "{\"properties\": {\"nickname\": \"test_nickname\"}, \"kakao_account\": {\"email\": \"test_email@example.com\"}}";
        URL url = mock(URL.class);

        // Mock HttpURLConnection behavior
        when(url.openConnection()).thenReturn(connection);
        when(connection.getResponseCode()).thenReturn(200);
        when(connection.getInputStream()).thenReturn(new ByteArrayInputStream(userInfoResponse.getBytes()));
        when(jsonObject.get(any(String.class))).thenReturn(jsonElement);
        when(jsonObject.getAsJsonObject()).thenReturn(jsonObject);

        Map<String,String> map = new HashMap<>();
        map.put("accessToken","test_accessToken");
        when(userService.kakaoLogin(any(UserKakaoInfoDTO.class))).thenReturn(map);

        Map<String, String> userInfo = oAuthServiceImpl.getUserInfo(kakaoAccessToken);

        //then
        assertThat(userInfo.get("accessToken")).isEqualTo("test_accessToken");
    }
}