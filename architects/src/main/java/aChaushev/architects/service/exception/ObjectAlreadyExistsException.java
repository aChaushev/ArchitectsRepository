package aChaushev.architects.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "That object already exists!")
public class ObjectAlreadyExistsException extends RuntimeException {
    private int statusCode;

    public ObjectAlreadyExistsException(String message) {
        super(message);
        this.statusCode = 403;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
