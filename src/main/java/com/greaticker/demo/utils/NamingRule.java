package com.greaticker.demo.utils;

import com.greaticker.demo.exception.customException.NotAllowedChracterException;

import static com.greaticker.demo.exception.errorCode.ErrorCode.NOT_ALLOWED_CHARACTER;

public class NamingRule {

    public static void validateNoSpecialCharacters(String value) {
        if (value.trim().isEmpty() || value.matches(".*[^ㄱ-ㅎ가-힣a-zA-Z0-9\\s].*")) {
            throw new NotAllowedChracterException(NOT_ALLOWED_CHARACTER);
        }
    }

    public static void validateNoSpecialCharactersContainingEvenSpace(String value) {
        if (value.trim().isEmpty() || value.matches(".*[^ㄱ-ㅎ가-힣a-zA-Z0-9].*")) {
            throw new NotAllowedChracterException(NOT_ALLOWED_CHARACTER);
        }
    }

    public static int calculateLength(String value) {
        int length = 0;
        for (char c : value.toCharArray()) {
            if (Character.toString(c).matches("[ㄱ-ㅎ가-힣]")) {
                // 한글인 경우 4으로 계산
                length += 4;
            } else if (Character.isUpperCase(c)) {
                // 영어 대문자인 경우 3으로 계산
                length += 3;
            } else if (Character.isLowerCase(c) || Character.isWhitespace(c) || Character.isDigit(c)) {
                // 영어 소문자이거나 공백인 경우 2로 계산
                length += 2;
            } else {
                // 특수문자가 포함된 경우 예외 발생
                throw new NotAllowedChracterException(NOT_ALLOWED_CHARACTER);
            }
        }
        return length;
    }


}
