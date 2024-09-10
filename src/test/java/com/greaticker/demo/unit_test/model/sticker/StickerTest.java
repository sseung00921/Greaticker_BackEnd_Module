package com.greaticker.demo.unit_test.model.sticker;

import com.greaticker.demo.model.sticker.Sticker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StickerTest {

    private Sticker sticker;

    @BeforeEach
    void setUp() {
        sticker = new Sticker();
        sticker.setHitCnt(0); // 초기 hitCnt를 0으로 설정
    }

    @Test
    void testPlusHit() {
        // Given
        int initialHitCnt = sticker.getHitCnt();

        // When
        sticker.plusHit();

        // Then
        assertEquals(initialHitCnt + 1, sticker.getHitCnt(), "Hit count should be incremented by 1");
    }

    @Test
    void testMinusHit_Success() {
        // Given
        sticker.setHitCnt(1); // 초기 hitCnt를 1로 설정
        int initialHitCnt = sticker.getHitCnt();

        // When
        sticker.minusHit();

        // Then
        assertEquals(initialHitCnt - 1, sticker.getHitCnt(), "Hit count should be decremented by 1");
    }

    @Test
    void testMinusHit_Failure() {
        // Given
        sticker.setHitCnt(0); // 초기 hitCnt를 0으로 설정

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> sticker.minusHit());
        assertEquals("sticker hit cnt can not be negative", exception.getMessage());
    }
}


