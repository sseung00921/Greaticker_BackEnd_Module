package com.greaticker.demo.unit_test.utils;

import com.greaticker.demo.utils.StringConverter;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StringConverterTest {

    @Test
    public void testLongToStringConvert_NonNullValue() {
        // Given
        Long input = 12345L;

        // When
        String result = StringConverter.longToStringConvert(input);

        // Then
        assertThat(result).isEqualTo("12345");
    }

    @Test
    public void testLongToStringConvert_NullValue() {
        // Given
        Long input = null;

        // When
        String result = StringConverter.longToStringConvert(input);

        // Then
        assertThat(result).isNull();
    }

    @Test
    public void testLongToStringConvert_ZeroValue() {
        // Given
        Long input = 0L;

        // When
        String result = StringConverter.longToStringConvert(input);

        // Then
        assertThat(result).isEqualTo("0");
    }

    @Test
    public void testLongToStringConvert_NegativeValue() {
        // Given
        Long input = -12345L;

        // When
        String result = StringConverter.longToStringConvert(input);

        // Then
        assertThat(result).isEqualTo("-12345");
    }
}

