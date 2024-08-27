package com.greaticker.demo.exception.customException;

public class NoSupportedProjectStateChangeException extends RuntimeException{
    public NoSupportedProjectStateChangeException(String message) {
        super(message);
    }
}
