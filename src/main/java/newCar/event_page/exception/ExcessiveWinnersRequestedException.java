package newCar.event_page.exception;

public class ExcessiveWinnersRequestedException extends RuntimeException {
    public ExcessiveWinnersRequestedException(String message) {
        super(message);
    }
}
