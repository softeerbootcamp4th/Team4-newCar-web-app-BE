package newCar.event_page.config;

import lombok.RequiredArgsConstructor;
import newCar.event_page.interceptor.AdminTokenInterceptor;
import newCar.event_page.interceptor.LoggingInterceptor;
import newCar.event_page.interceptor.SessionInterceptor;
import newCar.event_page.interceptor.UserTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LoggingInterceptor loggingInterceptor;
    private final AdminTokenInterceptor adminTokenInterceptor;
    private final UserTokenInterceptor userTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminTokenInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/login");

        registry.addInterceptor(loggingInterceptor);

        registry.addInterceptor(userTokenInterceptor)
                .addPathPatterns("/main/**")
                .excludePathPatterns("/main/login");
    }

    /*
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("http://www.batro.org", "http://*.batro.org:*", "https://*.batro.org:*", "http://1.231.159.76:*", "https://1.231.159.76:*", "http://*.batro.org/:*", "https://*.batro.org/:*")
                .allowCredentials(false)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .maxAge(3600);
    }*/


}