package com.greaticker.demo.exception.customException;

public class OverHitFavoriteLimitException extends RuntimeException{
    public OverHitFavoriteLimitException(String message) {
        super(message);
    }
}
