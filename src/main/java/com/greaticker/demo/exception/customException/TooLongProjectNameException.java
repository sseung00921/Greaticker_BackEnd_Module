package com.greaticker.demo.exception.customException;

public class TooLongProjectNameException extends RuntimeException{
    public TooLongProjectNameException(String message) {
        super(message);
    }
}
