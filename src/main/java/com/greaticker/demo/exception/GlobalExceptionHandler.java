package com.greaticker.demo.exception;

import com.greaticker.demo.dto.response.common.ApiResponse;
import com.greaticker.demo.exception.customException.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSupportedProjectStateChangeException.class)
    public ResponseEntity<ApiResponse> handleNoSupportedProjectStateChangeException(NoSupportedProjectStateChangeException ex, WebRequest request) {
        String errorMessage = ex.getMessage();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(false, errorMessage, null));
    }

    @ExceptionHandler(TooLongProjectNameException.class)
    public ResponseEntity<ApiResponse> handleTooLongProjectNameException(TooLongProjectNameException ex, WebRequest request) {
        String errorMessage = ex.getMessage();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(false, errorMessage, null));
    }

    @ExceptionHandler(TooShortProjectNameException.class)
    public ResponseEntity<ApiResponse> handleTooShortProjectNameException(TooShortProjectNameException ex, WebRequest request) {
        String errorMessage = ex.getMessage();
        System.out.println(errorMessage);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(false, errorMessage, null));
    }

    @ExceptionHandler(TodayStickerAlreadyGotException.class)
    public ResponseEntity<ApiResponse> handleTodayStickerAlreadyGotException(TodayStickerAlreadyGotException ex, WebRequest request) {
        String errorMessage = ex.getMessage();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(false, errorMessage, null));
    }

    @ExceptionHandler(NotAllowedChracterException.class)
    public ResponseEntity<ApiResponse> handleNotAllowedChracterException(NotAllowedChracterException ex, WebRequest request) {
        String errorMessage = ex.getMessage();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(false, errorMessage, null));
    }

    @ExceptionHandler(CanNotHitFavoriteBeforeGotAllStickerException.class)
    public ResponseEntity<ApiResponse> handleCanNotHitFavoriteBeforeGotAllStickerException(CanNotHitFavoriteBeforeGotAllStickerException ex, WebRequest request) {
        String errorMessage = ex.getMessage();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(false, errorMessage, null));
    }

    @ExceptionHandler(OverHitFavoriteLimitException.class)
    public ResponseEntity<ApiResponse> handleOverHitFavoriteLimitException(OverHitFavoriteLimitException ex, WebRequest request) {
        String errorMessage = ex.getMessage();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(false, errorMessage, null));
    }

    @ExceptionHandler(DuplicatedHallOfFameException.class)
    public ResponseEntity<ApiResponse> handleDuplicatedHallOfFameException(DuplicatedHallOfFameException ex, WebRequest request) {
        String errorMessage = ex.getMessage();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(false, errorMessage, null));
    }

    @ExceptionHandler(DuplicatedNicknameException.class)
    public ResponseEntity<ApiResponse> handleDuplicatedNicknameException(DuplicatedNicknameException ex, WebRequest request) {
        String errorMessage = ex.getMessage();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(false, errorMessage, null));
    }

    @ExceptionHandler(TooShortNicknameException.class)
    public ResponseEntity<ApiResponse> handleTooShortNicknameException(TooShortNicknameException ex, WebRequest request) {
        String errorMessage = ex.getMessage();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(false, errorMessage, null));
    }

    @ExceptionHandler(TooLongNicknameException.class)
    public ResponseEntity<ApiResponse> handleTooLongNicknameException(TooLongNicknameException ex, WebRequest request) {
        String errorMessage = ex.getMessage();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(false, errorMessage, null));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        String errorMessage = ex.getMessage();
        System.out.println(errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, errorMessage, null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleUnDefinedException(Exception ex, WebRequest request) {
        String errorMessage = ex.getMessage();
        System.out.println(errorMessage);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, errorMessage, null));
    }
}
