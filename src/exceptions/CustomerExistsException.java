package exceptions;

public class CustomerExistsException extends SalonException {
    public CustomerExistsException(String message) {
        super(message);
    }
}
