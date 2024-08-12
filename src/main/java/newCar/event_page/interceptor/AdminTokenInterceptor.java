package newCar.event_page.interceptor;

import lombok.RequiredArgsConstructor;
import newCar.event_page.jwt.JwtTokenProvider;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class AdminTokenInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setContentType("text/plain;charset=utf-8");

        String token = request.getHeader("Authorization");

        if(token==null){
            response.getWriter().write("관리자 로그인을 먼저 진행해주세요");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }//토큰없이 접근 시

        if(!jwtTokenProvider.validateToken(token)){
            response.getWriter().write("로그인이 만료 되었습니다");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }//만료된 토큰으로 접근시

        if(!jwtTokenProvider.validateAdminToken(token)){
            response.getWriter().write("유저는 접근 권한이 없습니다");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }//토큰은 있지만 admin토큰이 아닌 다른 토큰으로 접근 시

        return true;
    }
}