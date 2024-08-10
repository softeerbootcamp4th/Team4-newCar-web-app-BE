package newCar.event_page.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import newCar.event_page.config.JwtConfig;
import newCar.event_page.model.entity.Team;
import newCar.event_page.repository.jpa.UserLightRepository;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtTokenProviderImpl implements JwtTokenProvider{

    private final JwtConfig jwtConfig;
    private final UserLightRepository userLightRepository;

    public String generateToken(String name, boolean isAdmin){

        String role = isAdmin ? "admin" : "user";
        Long userId = userLightRepository.findByUserId(name);

        // 클레임 설정
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);  // 사용자 이름 추가
        claims.put("role", role);  // 역할 추가

        // 토큰 만료 시간 설정 (현재 시간 + 설정된 만료 시간)
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtConfig.getExpiration());

        // JWT 토큰 생성
        return Jwts.builder()
                .setClaims(claims)  // 클레임 설정
                .setSubject(name)  // 토큰 주제(일반적으로 사용자 이름)
                .setIssuedAt(now)  // 발급 시간 설정
                .setExpiration(expiryDate)  // 만료 시간 설정
                .signWith(Keys.hmacShaKeyFor(secretKey()), SignatureAlgorithm.HS256)  // 서명 키와 알고리즘 설정
                .compact();  // 토큰 생성
    }

    public String getUserId(String token){
        return "1234";
    } //토큰에서 유저 Id를 추출

    public Team getTeam(String token){
        return Team.PET;

    } //토큰에서 유저 Team을 추출

    public boolean validateToken(String token){
        try{
            Claims claims = Jwts.parserBuilder()
                                .setSigningKey(secretKey())
                                .build()
                                .parseClaimsJws(token)
                                .getBody();//페이로드 부분 추출
            return true;
        } catch (ExpiredJwtException | SignatureException e){
            return false;
        } //토큰이 만료되었거나 변조되었다면

    } //JWT 토큰 유효성 검증

    public boolean validateAdminToken(String token){
        String role = "";
        try{
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            role = claims.get("role",String.class);
            } catch (ExpiredJwtException | SignatureException e){
                return false;
            } //토큰이 만료되었거나 변조되었다면

        return role.equals("admin");//role 이 admin이라면 true를, 아니라면 false를 반환한다
    }
    //JWT 토큰이 admin의 역할을 담고 있는지 검증

    private byte[] secretKey(){
        return Decoders.BASE64.decode(jwtConfig.getSecret());
    }//원래는 secret key 값을 바로 바꿔 줄 수 있었으나 그 메소드는 deprecated 되어서
    //디코딩을 해주어야 합니다

}
