package newCar.event_page.jwt;

import newCar.event_page.model.enums.Team;

import java.util.Map;

public interface JwtTokenProvider {

    public String generateAdminToken();

    public String generateUserToken(String name);

    public String generateToken(Map<String,Object> claims);

    public Long getUserId(String token); //토큰에서 유저 Id를 추출

    public String getTeam(String token); //토큰에서 유저 Team을 추출

    public boolean validateToken(String token); //JWT 토큰 유효성 검증

    public boolean validateAdminToken(String token);//JWT 토큰이 admin의 역할을 담고 있는지 검증

    public String generateTokenWithTeam(Team team, String authorizationHeader);
}
