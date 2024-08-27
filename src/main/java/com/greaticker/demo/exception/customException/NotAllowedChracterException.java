package com.greaticker.demo.exception.customException;

public class NotAllowedChracterException extends RuntimeException{
    public NotAllowedChracterException(String message) {
        super(message);
    }
}
