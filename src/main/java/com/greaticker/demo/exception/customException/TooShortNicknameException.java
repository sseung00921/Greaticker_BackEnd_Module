package com.greaticker.demo.exception.customException;

public class TooShortNicknameException extends RuntimeException{
    public TooShortNicknameException(String message) {
        super(message);
    }
}
