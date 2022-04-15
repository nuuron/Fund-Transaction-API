package money.neowise.exception;

import org.springframework.http.HttpStatus;

public class TransactionException extends ApiException {

    public TransactionException(HttpStatus status) {
        super(status);
    }

    public TransactionException(HttpStatus status, String message) {
        super(status, message);
    }
}
