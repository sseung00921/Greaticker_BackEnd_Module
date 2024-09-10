package com.greaticker.demo.exception.customException;

public class DuplicatedNicknameException extends RuntimeException{
    public DuplicatedNicknameException(String message) {
        super(message);
    }
}
