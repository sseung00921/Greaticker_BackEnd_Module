package com.greaticker.demo.unit_test.utils;

import com.greaticker.demo.exception.customException.NotAllowedChracterException;
import com.greaticker.demo.utils.NamingRule;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.junit.jupiter.api.Assertions.*;

class NamingRuleTest {

    @Test
    void testValidateNoSpecialCharacters_ValidInputs() {
        // Arrange
        String validInput1 = "ValidName123";
        String validInput2 = "한글English123";
        String validInput3 = "Valid Name With Space";
        String validInput4 = "한글 With Space";

        // Act & Assert
        NamingRule.validateNoSpecialCharacters(validInput1);
        NamingRule.validateNoSpecialCharacters(validInput2);
        NamingRule.validateNoSpecialCharacters(validInput3);
        NamingRule.validateNoSpecialCharacters(validInput4);
    }

    @Test
    void testValidateNoSpecialCharacters_InvalidInputs() {
        // Arrange
        String invalidInput1 = "Invalid@Name";
        String invalidInput2 = "Invalid#Name";
        String invalidInput3 = "";
        String invalidInput4 = " ";

        // Act & Assert
        assertThatThrownBy(() -> NamingRule.validateNoSpecialCharacters(invalidInput1))
                .isInstanceOf(NotAllowedChracterException.class);

        assertThatThrownBy(() -> NamingRule.validateNoSpecialCharacters(invalidInput2))
                .isInstanceOf(NotAllowedChracterException.class);

        assertThatThrownBy(() -> NamingRule.validateNoSpecialCharacters(invalidInput3))
                .isInstanceOf(NotAllowedChracterException.class);

        assertThatThrownBy(() -> NamingRule.validateNoSpecialCharacters(invalidInput4))
                .isInstanceOf(NotAllowedChracterException.class);
    }

    @Test
    void testValidateNoSpecialCharactersContainingEvenSpace_ThrowsExceptionOnSpace() {
        // Arrange
        String valueWithSpace = "test value";

        // Act & Assert
        assertThatThrownBy(() -> NamingRule.validateNoSpecialCharactersContainingEvenSpace(valueWithSpace))
                .isInstanceOf(NotAllowedChracterException.class);
    }

    @Test
    void testCalculateLength_KoreanCharacters() {
        // Arrange
        String input = "가나다라";

        // Act
        int length = NamingRule.calculateLength(input);

        // Assert
        assertThat(length).isEqualTo(16);  // 4 characters * 4 length each
    }

    @Test
    void testCalculateLength_EnglishUppercase() {
        // Arrange
        String input = "ABC";

        // Act
        int length = NamingRule.calculateLength(input);

        // Assert
        assertThat(length).isEqualTo(9);  // 3 characters * 3 length each
    }

    @Test
    void testCalculateLength_EnglishLowercase() {
        // Arrange
        String input = "abc";

        // Act
        int length = NamingRule.calculateLength(input);

        // Assert
        assertThat(length).isEqualTo(6);  // 3 characters * 2 length each
    }

    @Test
    void testCalculateLength_Digits() {
        // Arrange
        String input = "123";

        // Act
        int length = NamingRule.calculateLength(input);

        // Assert
        assertThat(length).isEqualTo(6);  // 3 characters * 2 length each
    }

    @Test
    void testCalculateLength_MixedInput() {
        // Arrange
        String input = "가A1 b";

        // Act
        int length = NamingRule.calculateLength(input);

        // Assert
        assertThat(length).isEqualTo(13);  // 4 + 3 + 2 + 2 + 2 (한글 + 대문자 + 숫자 + 공백 + 소문자)
    }

    @Test
    void testCalculateLength_InvalidCharacter() {
        // Arrange
        String input = "Invalid@Name";

        // Act & Assert
        assertThatThrownBy(() -> NamingRule.calculateLength(input))
                .isInstanceOf(NotAllowedChracterException.class);
    }
}

