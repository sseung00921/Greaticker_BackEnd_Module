package com.greaticker.demo.exception.customException;

public class TooLongNicknameException extends RuntimeException{
    public TooLongNicknameException(String message) {
        super(message);
    }
}
