package newCar.event_page.jwt;

import newCar.event_page.model.entity.Team;

public interface JwtTokenProvider {

    public String generateAdminToken();

    public String generateUserToken(String name);

    public String generateToken(Long id,String role);

    public Long getUserId(String token); //토큰에서 유저 Id를 추출

    public Team getTeam(String token); //토큰에서 유저 Team을 추출

    public boolean validateToken(String token); //JWT 토큰 유효성 검증

    public boolean validateAdminToken(String token);//JWT 토큰이 admin의 역할을 담고 있는지 검증
}
