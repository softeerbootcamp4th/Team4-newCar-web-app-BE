package newCar.event_page.interceptor;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import newCar.event_page.service.session.SessionService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class SessionInterceptor implements HandlerInterceptor {

    private final SessionService sessionService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String sessionId = null;
        response.setContentType("text/plain;charset=utf-8");
        // 쿠키에서 세션 ID 찾기
        if(request.getCookies() == null) {
            response.getWriter().write("접근 권한이 없습니다.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        for (Cookie cookie : request.getCookies()) {
            if ("session".equals(cookie.getName())) {
                sessionId = cookie.getValue();
                break;
            }
        }

        if(sessionId == null){
            response.getWriter().write("접근 권한이 없습니다.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        if (!sessionService.validateSession(sessionId)) {
            response.getWriter().write("세션이 만료되었습니다.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        return true;
    }
}