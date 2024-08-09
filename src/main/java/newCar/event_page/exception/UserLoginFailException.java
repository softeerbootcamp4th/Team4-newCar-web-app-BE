package newCar.event_page.exception;

public class UserLoginFailException extends RuntimeException{
    public UserLoginFailException(String message) {
        super(message);
    }
}