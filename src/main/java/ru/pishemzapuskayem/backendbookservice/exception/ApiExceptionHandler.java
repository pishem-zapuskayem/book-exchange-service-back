package ru.pishemzapuskayem.backendbookservice.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiExceptionDTO> handlerException(ApiException apiException) {
        return new ResponseEntity<ApiExceptionDTO>(
                new ApiExceptionDTO(
                        apiException.getMessage(),
                        apiException.getStatus()
                ),
                apiException.getStatus()
        );
    }
}
