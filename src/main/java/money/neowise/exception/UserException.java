package money.neowise.exception;

import org.springframework.http.HttpStatus;

public class UserException extends ApiException {

    public UserException(HttpStatus status) {
        super(status);
    }

    public UserException(HttpStatus status, String message) {
        super(status, message);
    }
}
