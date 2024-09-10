package com.greaticker.demo.exception.customException;

public class AlreadyResetProjectException extends RuntimeException{
    public AlreadyResetProjectException(String message) {
        super(message);
    }
}
