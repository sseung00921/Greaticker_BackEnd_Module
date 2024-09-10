package com.greaticker.demo.unit_test.model.hallOfFame;

import com.greaticker.demo.model.hallOfFame.HallOfFame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HallOfFameTest {

    private HallOfFame hallOfFame;

    @BeforeEach
    void setUp() {
        hallOfFame = new HallOfFame();
        hallOfFame.setHitCnt(0); // 초기 hitCnt를 0으로 설정
    }

    @Test
    void testPlusHitCnt() {
        // Given
        int initialHitCnt = hallOfFame.getHitCnt();

        // When
        hallOfFame.plusHitCnt();

        // Then
        assertEquals(initialHitCnt + 1, hallOfFame.getHitCnt(), "Hit count should be incremented by 1");
    }

    @Test
    void testMinusHit_Success() {
        // Given
        hallOfFame.setHitCnt(1); // 초기 hitCnt를 1로 설정
        int initialHitCnt = hallOfFame.getHitCnt();

        // When
        hallOfFame.minusHit();

        // Then
        assertEquals(initialHitCnt - 1, hallOfFame.getHitCnt(), "Hit count should be decremented by 1");
    }

    @Test
    void testMinusHit_Failure() {
        // Given
        hallOfFame.setHitCnt(0); // 초기 hitCnt를 0으로 설정

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> hallOfFame.minusHit());
        assertEquals("hall of fame hit cnt can not be negative", exception.getMessage());
    }
}

