package newCar.event_page.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import newCar.event_page.config.JwtConfig;
import newCar.event_page.exception.UnverifiedTokenException;
import newCar.event_page.model.enums.Team;
import newCar.event_page.model.entity.User;
import newCar.event_page.repository.jpa.UserRepository;
import org.springframework.stereotype.Component;

import java.security.SignatureException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class JwtTokenProviderImpl implements JwtTokenProvider{

    private final JwtConfig jwtConfig;

    private final UserRepository userRepository;

    @Override
    public String generateAdminToken(){
        // 클레임 설정
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", 1L);  // 사용자 아이디 추가
        claims.put("role", "admin");  // 역할 추가
        claims.put("team", null);

        return generateToken(claims);
    }

    @Override
    public String generateUserToken(String userName){

        User user = userRepository.findByUserName(userName).
                orElseThrow(() -> new NoSuchElementException("잘못된 유저 정보입니다"));

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());  // 사용자 아이디 추가
        claims.put("role", "user");  // 역할 추가
        claims.put("team", user.getTeam()== null ? "" : user.getTeam().toString());

        return generateToken(claims);
    }

    @Override
    public String generateToken(Map<String, Object> claims){

        // 토큰 만료 시간 설정 (현재 시간 + 설정된 만료 시간)
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtConfig.getExpiration());

        // JWT 토큰 생성
        return Jwts.builder()
                .setClaims(claims)  // 클레임 설정
                .setSubject(claims.get("role").toString())  // 토큰 주제
                .setIssuedAt(now)  // 발급 시간 설정
                .setExpiration(expiryDate)  // 만료 시간 설정
                .signWith(Keys.hmacShaKeyFor(secretKey()), SignatureAlgorithm.HS256)  // 서명 키와 알고리즘 설정
                .compact();  // 토큰 생성
    }

    @Override
    public String generateTokenWithTeam(Team team, String authorizationHeader){

        Long userId;
        try {
            userId = getClaims(authorizationHeader).get("userId", Long.class);
        } catch (Exception e) {
            throw new UnverifiedTokenException("잘못된 토큰입니다.");
        }

        Map<String, Object> claims = new HashMap<>();

        claims.put("userId", userId);  // 사용자 아이디 추가
        claims.put("role", "user");  // 역할 추가
        claims.put("team" , team.toString());
        return generateToken(claims);
    }//성격 유형검사가 끝났을 때 다시 team값을 설정해줘서 엑세스토큰을 새로 발급해준다

    @Override
    public Long getUserId(String token){
        try{
            return getClaims(token).get("userId", Long.class);
        } catch (Exception e){
            throw new UnverifiedTokenException("토큰 검증을 먼저 진행해야합니다.");
        }
    } //토큰에서 유저 Id를 추출

    @Override
    public Team getTeam(String token){
        try{
            return claimsToTeam(getClaims(token).get("team", String.class));
        } catch (Exception e){
            throw new UnverifiedTokenException("토큰 검증을 먼저 진행해야합니다.");
        }
    } //토큰에서 유저 Team을 추출

    @Override
    public boolean validateToken(String token){
        try{
            getClaims(token);
            return true;
        } catch (Exception e){
            return false;
        } //토큰이 만료되었거나 변조되었다면
    } //JWT 토큰 유효성 검증

    @Override
    public boolean validateAdminToken(String token){
        try{
            return getClaims(token).get("role", String.class).equals("admin");
        } catch (Exception e){
            return false;
        }
    }
    //JWT 토큰이 admin의 역할을 담고 있는지 검증

    private Team claimsToTeam(String team){

        switch(team){
            case "PET" : return Team.PET;
            case "TRAVEL" : return Team.TRAVEL;
            case "LEISURE" : return Team.LEISURE;
            case "SPACE" : return Team.SPACE;
            default: return Team.PET;
        }
    }

    private Claims getClaims(String token)
            throws ExpiredJwtException, SignatureException, IllegalArgumentException, MalformedJwtException, UnsupportedJwtException {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    byte[] secretKey(){
        return Decoders.BASE64.decode(jwtConfig.getSecret());
    }//원래는 secret key 값을 바로 바꿔 줄 수 있었으나 그 메소드는 deprecated 되어서
    //디코딩을 해주어야 합니다

}
