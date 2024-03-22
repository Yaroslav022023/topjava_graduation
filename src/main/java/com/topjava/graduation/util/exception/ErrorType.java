package com.topjava.graduation.util.exception;

import org.springframework.http.HttpStatus;

public enum ErrorType {
    DATA_NOT_FOUND(HttpStatus.UNPROCESSABLE_ENTITY),
    VOTING_RESTRICTIONS(HttpStatus.UNPROCESSABLE_ENTITY),
    VALIDATION_ERROR(HttpStatus.UNPROCESSABLE_ENTITY),
    DATA_ERROR(HttpStatus.CONFLICT),
    WRONG_REQUEST(HttpStatus.BAD_REQUEST),
    APP_ERROR(HttpStatus.INTERNAL_SERVER_ERROR);

    private final HttpStatus status;

    ErrorType(HttpStatus status) {
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
