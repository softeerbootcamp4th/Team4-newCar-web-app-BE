package newCar.event_page.jwt;

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

    } //토큰에서 유저 Team을 추출

    public boolean validateToken(String token){

    } //JWT 토큰 유효성 검증

    public boolean validateAdminToken(String token){

    }//JWT 토큰이 admin의 역할을 담고 있는지 검증

}
