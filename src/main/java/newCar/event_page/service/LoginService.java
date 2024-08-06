package newCar.event_page.service;

import lombok.RequiredArgsConstructor;
import newCar.event_page.exception.AdminLoginFailException;
import newCar.event_page.model.dto.AdministratorDTO;
import newCar.event_page.model.entity.Administrator;
import newCar.event_page.repository.AdministratorRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final AdministratorRepository administratorRepository;

    public String admin(AdministratorDTO administratorDTO){
        Administrator administrator = administratorRepository.findById(1L)
                .orElseThrow(() -> new NoSuchElementException("관리자 정보가 존재하지 않습니다."));

        if(!isLoginSuccess(administrator, administratorDTO)){
            throw new AdminLoginFailException("아이디 혹은 비밀번호가 맞지 않습니다.");
        }

        //TODO 로그인 세션 주는 로직 추가

        return "관리자 로그인 성공";
    }

    private boolean isLoginSuccess(Administrator admin, AdministratorDTO dto){
        if(!admin.getAdminId().equals(dto.getAdminId())) return false;
        if(!admin.getPassword().equals(dto.getPassword())) return false;
        return true;
    }

}
