package com.greaticker.demo.exception.customException;

public class TodayStickerAlreadyGotException extends RuntimeException{
    public TodayStickerAlreadyGotException(String message) {
        super(message);
    }
}
