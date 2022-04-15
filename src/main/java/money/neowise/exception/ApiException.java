package money.neowise.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public abstract class ApiException extends Exception {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timeStamp;
    private HttpStatus status;
    private String message;

    private ApiException() {
        this.timeStamp = LocalDateTime.now(ZoneOffset.UTC);
    }

    public ApiException(HttpStatus status) {
        this();
        this.status = status;
        this.message = "Unexpected Error Occurred";
    }

    public ApiException(HttpStatus status, String message) {
        this();
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    // this is a workaround way to prevent the stackTrace from keep getting included with the error response
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }


}
