package newCar.event_page.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import newCar.event_page.config.JwtConfig;
import newCar.event_page.exception.UnverifiedTokenException;
import newCar.event_page.model.entity.User;
import newCar.event_page.model.enums.Team;
import newCar.event_page.repository.jpa.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.security.Key;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class JwtTokenProviderImplTest {

    @Mock
    private JwtConfig jwtConfig;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private JwtTokenProviderImpl jwtTokenProvider;

    private String secretKey;
    private Key key;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up secret key and expiration time for testing
        secretKey = "mySecretKeyWhichIsVerySecretAndMustBeLongEnough123456";
        when(jwtConfig.getSecret()).thenReturn(secretKey);
        when(jwtConfig.getExpiration()).thenReturn(3600000L); // 1 hour in milliseconds

        // Generate a signing key from the secret key
        key = Keys.hmacShaKeyFor(jwtTokenProvider.secretKey());
    }

    @Test
    @DisplayName("adminToken 발급_성공")
    void generateAdminToken_Success() {
        // When
        String token = jwtTokenProvider.generateAdminToken();

        // Then
        assertThat(token).isNotNull();
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        assertThat(claims.get("role")).isEqualTo("admin");
        assertThat(claims.get("userId")).isEqualTo(1);
        assertThat(claims.get("team")).isNull();
    }

    @Test
    @DisplayName("유저 토큰 발급_성공")
    void generateUserToken_Success() {
        // Given
        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(user.getTeam()).thenReturn(Team.PET);
        when(userRepository.findByUserName("testUser")).thenReturn(Optional.of(user));

        // When
        String token = jwtTokenProvider.generateUserToken("testUser");

        // Then
        assertThat(token).isNotNull();
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        assertThat(claims.get("role")).isEqualTo("user");
        assertThat(claims.get("userId")).isEqualTo(1);
        assertThat(claims.get("team")).isEqualTo("PET");
    }

    @Test
    @DisplayName("유저 토큰 발급_실패")
    void generateUserToken_Fail() {
        // Given
        when(userRepository.findByUserName("testUser")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(()->jwtTokenProvider.generateUserToken("testUser"))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("성격유형검사 시 팀이 들어있는 토큰을 유저에 발급_성공")
    void generateTokenWithTeam_Success() {
        // Given
        String token = jwtTokenProvider.generateAdminToken(); // Generate an admin token to get userId
        Team team = Team.TRAVEL;

        // When
        String newToken = jwtTokenProvider.generateTokenWithTeam(team, token);

        // Then
        assertThat(newToken).isNotNull();
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(newToken).getBody();
        assertThat(claims.get("role")).isEqualTo("user");
        assertThat(claims.get("userId")).isEqualTo(1);
        assertThat(claims.get("team")).isEqualTo("TRAVEL");
    }

    @Test
    @DisplayName("성격유형검사 시 팀이 들어있는 토큰을 유저에 발급_실패")
    void generateTokenWithTeam_Fail() {

        Team team = Team.TRAVEL;

        // Then
        assertThatThrownBy(()->jwtTokenProvider.generateTokenWithTeam(team,"adsldaslf"))
                .isInstanceOf(UnverifiedTokenException.class);
    }

    @Test
    @DisplayName("토큰에서 userId Get_성공")
    void getUserId_Success() {
        // Given
        String token = jwtTokenProvider.generateAdminToken();

        // When
        Long userId = jwtTokenProvider.getUserId(token);

        // Then
        assertThat(userId).isEqualTo(1L);
    }

    @Test
    @DisplayName("토큰에서 userId Get _실패")
    void getUserId_Fail() {
        // Given
        String invalidToken = "invalidToken";

        // When & Then
        assertThatThrownBy(()->jwtTokenProvider.getUserId(invalidToken))
                .isInstanceOf(UnverifiedTokenException.class);
    }

    @Test
    @DisplayName("Team LEISURE 반환")
    void getTeam_LEISURE() {
        // Given
        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(user.getTeam()).thenReturn(Team.LEISURE);
        when(userRepository.findByUserName("testUser")).thenReturn(Optional.of(user));
        String token = jwtTokenProvider.generateUserToken("testUser");

        // When
        Team team = jwtTokenProvider.getTeam(token);

        // Then
        assertThat(team).isEqualTo(Team.LEISURE);
    }
    @Test
    @DisplayName("Team SPACE 반환")
    void getTeam_SPACE() {
        // Given
        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(user.getTeam()).thenReturn(Team.SPACE);
        when(userRepository.findByUserName("testUser")).thenReturn(Optional.of(user));
        String token = jwtTokenProvider.generateUserToken("testUser");

        // When
        Team team = jwtTokenProvider.getTeam(token);

        // Then
        assertThat(team).isEqualTo(Team.SPACE);
    }
    @Test
    @DisplayName("Team PET 반환")
    void getTeam_PET() {
        // Given
        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(user.getTeam()).thenReturn(Team.PET);
        when(userRepository.findByUserName("testUser")).thenReturn(Optional.of(user));
        String token = jwtTokenProvider.generateUserToken("testUser");

        // When
        Team team = jwtTokenProvider.getTeam(token);

        // Then
        assertThat(team).isEqualTo(Team.PET);
    }
    @Test
    @DisplayName("Team TRAVEL 반환")
    void getTeam_TRAVEL() {
        // Given
        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(user.getTeam()).thenReturn(Team.TRAVEL);
        when(userRepository.findByUserName("testUser")).thenReturn(Optional.of(user));
        String token = jwtTokenProvider.generateUserToken("testUser");

        // When
        Team team = jwtTokenProvider.getTeam(token);

        // Then
        assertThat(team).isEqualTo(Team.TRAVEL);
    }

    @Test
    @DisplayName("Team default 반환")
    void getTeam_Null() {
        // Given
        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(user.getTeam()).thenReturn(null);
        when(userRepository.findByUserName("testUser")).thenReturn(Optional.of(user));
        String token = jwtTokenProvider.generateUserToken("testUser");

        // When
        Team team = jwtTokenProvider.getTeam(token);

        // Then
        assertThat(team).isEqualTo(Team.PET);
    }

    @Test
    @DisplayName("토큰에서 team get_실패")
    void getTeam_Fail() {
        // Given
        String invalidToken = "invalidToken";

        // When & Then
        assertThatThrownBy(() -> jwtTokenProvider.getTeam(invalidToken))
                .isInstanceOf(UnverifiedTokenException.class);
    }

    @Test
    @DisplayName("토큰 유효성 검사_성공")
    void validateToken_shouldReturnTrue_whenTokenIsValid() {
        // Given
        String token = jwtTokenProvider.generateAdminToken();

        // When
        boolean isValid = jwtTokenProvider.validateToken(token);

        // Then
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("토큰 유효성 검사_실패")
    void validateToken_shouldReturnFalse_whenTokenIsInvalid() {
        // Given
        String invalidToken = "invalidToken";

        // When
        boolean isValid = jwtTokenProvider.validateToken(invalidToken);

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("admin토큰인지 확인_성공")
    void validateAdminToken_Success() {
        // Given
        String token = jwtTokenProvider.generateAdminToken();

        // When
        boolean isAdmin = jwtTokenProvider.validateAdminToken(token);

        // Then
        assertThat(isAdmin).isTrue();
    }

    @Test
    @DisplayName("admin토큰인지 확인_실패")
    void validateAdminToken_Fail() {
        // Given
        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(userRepository.findByUserName("testUser")).thenReturn(Optional.of(user));
        String token = jwtTokenProvider.generateUserToken("testUser");

        // When
        boolean isAdmin = jwtTokenProvider.validateAdminToken(token);

        // Then
        assertThat(isAdmin).isFalse();
    }
}
