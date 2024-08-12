package newCar.event_page.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import newCar.event_page.jwt.JwtTokenProvider;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class UserTokenInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setContentType("text/plain;charset=utf-8");

        String token = request.getHeader("Authorization");

        if(token==null){
            response.getWriter().write("로그인을 먼저 진행해주세요");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }//토큰없이 접근 시

        if(!jwtTokenProvider.validateToken(token)){
            response.getWriter().write("로그인이 만료 되었습니다");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }//만료된 토큰으로 접근시

        System.out.println("팀 이름 : "+jwtTokenProvider.getTeam(token)+" userId ( 인덱스) : " + jwtTokenProvider.getUserId(token));
        //getTeam , getUserId 테스트용

        return true;
    }
}