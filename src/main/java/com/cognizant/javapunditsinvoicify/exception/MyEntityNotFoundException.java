package com.cognizant.javapunditsinvoicify.exception;

public class MyEntityNotFoundException extends RuntimeException {

    public MyEntityNotFoundException() {
    }

    public MyEntityNotFoundException(String message) {
        super(message);
    }
}
