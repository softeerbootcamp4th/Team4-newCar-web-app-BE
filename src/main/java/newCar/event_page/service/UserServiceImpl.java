package newCar.event_page.service;

import lombok.RequiredArgsConstructor;
import newCar.event_page.exception.AdminLoginFailException;
import newCar.event_page.exception.UserLoginFailException;
import newCar.event_page.jwt.JwtTokenProvider;
import newCar.event_page.model.dto.user.UserLightDTO;
import newCar.event_page.model.entity.UserLight;
import newCar.event_page.model.session.Session;
import newCar.event_page.model.session.UserSession;
import newCar.event_page.repository.jpa.UserLightRepository;
import newCar.event_page.repository.redis.SessionRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserLightRepository userLightRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public ResponseEntity<String> login(UserLightDTO userLightDTO) {
        UserLight userLight = userLightRepository.findById(1L)
                .orElseThrow(() -> new NoSuchElementException("유저 정보가 존재하지 않습니다"));

        if(!isUserLoginSuccess(userLight, userLightDTO)){
            throw new UserLoginFailException("아이디 혹은 비밀번호가 맞지 않습니다.");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, jwtTokenProvider.generateToken(userLight.getUserId(),false));
        //로그인 성공시 토큰을 발급해서 준다
        //역할이 user인 토큰을 발급받는다

        return new ResponseEntity<>("유저 로그인 성공", headers, HttpStatus.OK);
    }

    private boolean isUserLoginSuccess(UserLight userLight, UserLightDTO dto){
        if(!userLight.getUserId().equals(dto.getUserId())) return false;
        if(!userLight.getPassword().equals(dto.getPassword())) return false;
        return true;
    }
}
