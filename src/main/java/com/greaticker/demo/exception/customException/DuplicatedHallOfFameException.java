package com.greaticker.demo.exception.customException;

public class DuplicatedHallOfFameException extends RuntimeException{
    public DuplicatedHallOfFameException(String message) {
        super(message);
    }
}
