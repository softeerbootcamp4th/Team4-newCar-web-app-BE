package newCar.event_page.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import newCar.event_page.config.JwtConfig;
import newCar.event_page.model.entity.Team;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenProviderImpl implements JwtTokenProvider{

    private final JwtConfig jwtConfig;

    public String generateToken(String username, boolean isAdmin){

    }

    public String getUserId(String token){

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
