package com.swisherdominicana.molde.exception

import org.springframework.http.HttpStatus

class CustomException extends RuntimeException {

    private static final long serialVersionUID = 1L

    private final String message
    private final HttpStatus httpStatus

    CustomException(String message, HttpStatus httpStatus) {
        this.message = message
        this.httpStatus = httpStatus
    }

    @Override
     String getMessage() {
        return message
    }

    HttpStatus getHttpStatus() {
        return httpStatus
    }
}
