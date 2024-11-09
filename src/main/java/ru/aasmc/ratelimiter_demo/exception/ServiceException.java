package ru.aasmc.ratelimiter_demo.exception;

import org.springframework.http.HttpStatus;

public class ServiceException extends RuntimeException{

    private final HttpStatus status;

    public ServiceException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
