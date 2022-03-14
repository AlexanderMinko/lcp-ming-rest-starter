package com.lenovo.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerController {

    @ExceptionHandler({ProductNotFound.class})
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException exception) {
        var errorResponse = ErrorResponse.builder()
                .message(exception.getMessage())
                .status(BAD_REQUEST.value())
                .timeStamp(System.currentTimeMillis())
                .build();
        log.error(exception.getMessage());
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

}
