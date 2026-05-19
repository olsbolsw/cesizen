package fr.cesi.cesizen.exception;

import org.springframework.http.HttpStatus;

public class BusinessException extends ApiException {

    public BusinessException(ErrorCode code, HttpStatus status, String message) {
        super(code, status, message);
    }
}
