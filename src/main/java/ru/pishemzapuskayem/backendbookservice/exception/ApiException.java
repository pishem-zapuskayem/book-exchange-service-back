package ru.pishemzapuskayem.backendbookservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {
    private final HttpStatus status;

    public ApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public ApiException() {
        super(HttpStatus.BAD_REQUEST.getReasonPhrase());
        status = HttpStatus.BAD_REQUEST;
    }

    public ApiException(String msg) {
        super(msg);
        status = HttpStatus.BAD_REQUEST;
    }
}
