package newCar.event_page.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class JwtConfig {

    @Value("${jwt.secret}")
    private String secret;  // 비밀키

    @Value("${jwt.expiration}")
    private long expiration;    // 토큰 만료 시간

    @Value("${jwt.prefix}")
    private String prefix;  // 접두사
}
