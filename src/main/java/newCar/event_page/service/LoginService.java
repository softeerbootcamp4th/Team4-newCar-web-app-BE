package newCar.event_page.service;

import lombok.RequiredArgsConstructor;
import newCar.event_page.exception.AdminLoginFailException;
import newCar.event_page.service.session.AdminSession;
import newCar.event_page.service.session.Session;
import newCar.event_page.service.session.SessionStorage;
import newCar.event_page.model.dto.AdministratorDTO;
import newCar.event_page.model.entity.Administrator;
import newCar.event_page.repository.AdministratorRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final AdministratorRepository administratorRepository;
    private final SessionStorage sessionStorage;

    public ResponseEntity<String> admin(AdministratorDTO administratorDTO){
        Administrator administrator = administratorRepository.findById(1L)
                .orElseThrow(() -> new NoSuchElementException("관리자 정보가 존재하지 않습니다."));

        if(!isAdminLoginSuccess(administrator, administratorDTO)){
            throw new AdminLoginFailException("아이디 혹은 비밀번호가 맞지 않습니다.");
        }

        String sessionId = UUID.randomUUID().toString();
        Session session = new AdminSession(sessionId);

        ResponseCookie cookie = ResponseCookie.from("session", sessionId)
                .httpOnly(true)
                .path("/admin")
                .maxAge(60 * 30)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

        sessionStorage.addSession(session);

        return new ResponseEntity<>("관리자 로그인 성공", headers, HttpStatus.OK);
    }

    private boolean isAdminLoginSuccess(Administrator admin, AdministratorDTO dto){
        if(!admin.getAdminId().equals(dto.getAdminId())) return false;
        if(!admin.getPassword().equals(dto.getPassword())) return false;
        return true;
    }

}
