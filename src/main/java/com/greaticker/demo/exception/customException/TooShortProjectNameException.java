package com.greaticker.demo.exception.customException;

public class TooShortProjectNameException extends RuntimeException{
    public TooShortProjectNameException(String message) {
        super(message);
    }
}
