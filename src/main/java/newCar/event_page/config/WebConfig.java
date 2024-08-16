package newCar.event_page.config;

import lombok.RequiredArgsConstructor;
import newCar.event_page.interceptor.AdminTokenInterceptor;
import newCar.event_page.interceptor.LoggingInterceptor;
import newCar.event_page.interceptor.UserTokenInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.filter.CorsFilter;

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
                .excludePathPatterns("/main/login")
                .excludePathPatterns("/main/event-time")
                .excludePathPatterns("/main/quiz")
                .excludePathPatterns("/main/dummy-token")
                .excludePathPatterns("/main/kakao")
                .excludePathPatterns("/main/share-link/{url}");
    }


    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean() {
        FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CorsFilter(corsConfigurationSource()));
        registrationBean.setOrder(0);
        return registrationBean;
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:5173");
        config.addAllowedOrigin("https://admin.batro.org");
        config.addAllowedOrigin("https://www.batro.org");
        config.addAllowedOrigin("http://admin.batro.org");
        config.addAllowedOrigin("http://www.batro.org");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return source;
    }


    /*
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")
                .allowedOrigins("https://admin.batro.org")
                .allowedOrigins("https://www.batro.org")
                .allowedOrigins("http://admin.batro.org")
                .allowedOrigins("http://www.batro.org")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .maxAge(3600);
    }*/


}