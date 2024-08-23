package newCar.event_page.exception;

import newCar.event_page.exception.FCFS.FCFSNotStartedYet;
import newCar.event_page.exception.FCFS.FCFSNotYetConductedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(final NoSuchElementException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        //Jackson 라이브러리
    }

    @ExceptionHandler({
            UnmodifiableFieldException.class,
            ExcessiveWinnersRequestedException.class,
            DrawNotYetConductedException.class,
            AdminLoginFailException.class,
            UserLoginFailException.class,
            FCFSNotYetConductedException.class,
            UserAlreadyHasTeamException.class,
            IndexOutOfBoundsException.class
    })
    public ResponseEntity<String> handleUnmodifiableFieldException(final RuntimeException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(FCFSNotStartedYet.class)
    public ResponseEntity<String> handleFCFSNotStartedException(final RuntimeException e){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

}
