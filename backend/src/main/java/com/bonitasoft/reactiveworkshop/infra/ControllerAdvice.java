package com.bonitasoft.reactiveworkshop.infra;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.bonitasoft.reactiveworkshop.exception.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = BAD_REQUEST)
    public ResponseEntity handleIllegalArgumentException(NotFoundException e) {
        return ResponseEntity.notFound().build();
    }

}
