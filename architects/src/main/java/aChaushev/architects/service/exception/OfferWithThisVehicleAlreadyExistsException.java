package aChaushev.architects.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Offer with this project already exists.")
public class OfferWithThisVehicleAlreadyExistsException extends RuntimeException {
    private int statusCode;

    public OfferWithThisVehicleAlreadyExistsException() {
        this.statusCode = 409;
    }

    public OfferWithThisVehicleAlreadyExistsException(String message) {
        super(message);
        this.statusCode = 409;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
