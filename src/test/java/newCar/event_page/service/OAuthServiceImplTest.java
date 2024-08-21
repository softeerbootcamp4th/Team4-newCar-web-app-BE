package newCar.event_page.service;

import newCar.event_page.config.OAuthConfig;
import newCar.event_page.model.dto.user.UserKakaoInfoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OAuthServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private OAuthConfig oauthConfig;

    @Mock
    private URL url;

    @Mock
    private HttpURLConnection connection;

    @InjectMocks
    private OAuthServiceImpl oAuthService;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        when(url.openConnection()).thenReturn(connection);
    }

    @Test
    public void testGetAccessToken_Success() throws Exception {
        String code = "test_code";

        // Mock OAuthConfig
        when(oauthConfig.getClientId()).thenReturn("test_client_id");
        when(oauthConfig.getClientSecretId()).thenReturn("test_client_secret");
        when(oauthConfig.getRedirectUri()).thenReturn("test_redirect_uri");

        // Mock HttpURLConnection behavior
        when(connection.getResponseCode()).thenReturn(200);
        when(connection.getInputStream()).thenReturn(new java.io.ByteArrayInputStream(
                "{\"access_token\":\"test_access_token\"}".getBytes()));

        String accessToken = oAuthService.getAccessToken(code);

        // Verify that the access token is correctly parsed
        assertEquals("test_access_token", accessToken);

        verify(connection, times(1)).setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        verify(connection, times(1)).setDoOutput(true);
    }

    @Test
    public void testGetUserInfo_Success() throws Exception {
        String kakaoAccessToken = "test_kakao_access_token";

        // Mock HttpURLConnection behavior
        when(connection.getResponseCode()).thenReturn(200);
        when(connection.getInputStream()).thenReturn(new java.io.ByteArrayInputStream(
                ("{\"properties\":{\"nickname\":\"test_nickname\"},"
                        + "\"kakao_account\":{\"email\":\"test_email\"}}").getBytes()));

        // Mock UserService
        Map<String, String> mockResponse = new HashMap<>();
        mockResponse.put("status", "success");
        when(userService.kakaoLogin(any(UserKakaoInfoDTO.class))).thenReturn(mockResponse);

        Map<String, String> response = oAuthService.getUserInfo(kakaoAccessToken);

        // Verify that the user info is correctly parsed and passed to the UserService
        assertEquals("success", response.get("status"));

        verify(connection, times(1)).setRequestMethod("GET");
        verify(connection, times(1)).setRequestProperty("Authorization", "Bearer " + kakaoAccessToken);
        verify(connection, times(1)).setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
    }
}