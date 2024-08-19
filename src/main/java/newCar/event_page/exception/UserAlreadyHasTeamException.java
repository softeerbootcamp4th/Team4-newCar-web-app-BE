package newCar.event_page.exception;

public class UserAlreadyHasTeamException extends RuntimeException{
    public UserAlreadyHasTeamException(String message) {
        super(message);
    }
}
