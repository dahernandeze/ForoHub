package alura.forohub.api.exception;


public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}