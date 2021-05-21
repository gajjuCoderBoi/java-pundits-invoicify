package com.cognizant.javapunditsinvoicify.exception;

import com.cognizant.javapunditsinvoicify.response.ResponseMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({MyEntityNotFoundException.class})
    public ResponseEntity<ResponseMessage> handleMyEntityNotFoundException(MyEntityNotFoundException ex) {
        return new ResponseEntity<>(
                ResponseMessage.builder()
                        .httpStatus(NOT_FOUND)
                        .responseMessage(ex.getMessage())
                        .build(),
                NOT_FOUND);
    }

    @ExceptionHandler({InvalidDataException.class})
    public ResponseEntity<ResponseMessage> handleInvalidDataException(InvalidDataException ex) {
        return new ResponseEntity<>(
                ResponseMessage.builder()
                        .httpStatus(BAD_REQUEST)
                        .responseMessage(ex.getMessage())
                        .build(),
                BAD_REQUEST);
    }

}
