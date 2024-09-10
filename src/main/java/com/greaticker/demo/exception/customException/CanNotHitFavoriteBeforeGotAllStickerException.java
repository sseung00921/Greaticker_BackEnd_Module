package com.greaticker.demo.exception.customException;

public class CanNotHitFavoriteBeforeGotAllStickerException extends RuntimeException{
    public CanNotHitFavoriteBeforeGotAllStickerException(String message) {
        super(message);
    }
}
