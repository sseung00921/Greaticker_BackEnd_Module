package com.greaticker.demo.intergration_test.service.popularChart;

import com.greaticker.demo.dto.response.common.CursorPagination;
import com.greaticker.demo.dto.response.sticker.StickerRankingResponse;
import com.greaticker.demo.model.sticker.Sticker;
import com.greaticker.demo.repository.sticker.StickerRepository;
import com.greaticker.demo.service.popularChart.PopularChartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static com.greaticker.demo.constants.StickerCnt.TOTAL_STICKER_CNT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class PopularChartServiceTest {

    @MockBean
    private StickerRepository stickerRepository;

    @Autowired
    private PopularChartService popularChartService;

    private Sticker sticker1;
    private Sticker sticker2;

    @BeforeEach
    void setUp() {
        sticker1 = new Sticker();
        sticker1.setId(1L);
        sticker1.setName("Sticker 1");
        sticker1.setHitCnt(100);

        sticker2 = new Sticker();
        sticker2.setId(2L);
        sticker2.setName("Sticker 2");
        sticker2.setHitCnt(50);
    }

    @Test
    void testShowStickerRanking() {
        // Arrange
        List<Sticker> stickers = Arrays.asList(sticker1, sticker2);
        when(stickerRepository.findAllByOrderByHitCntDesc()).thenReturn(stickers);

        // Act
        CursorPagination<StickerRankingResponse> response = popularChartService.showStickerRanking();

        // Assert
        assertNotNull(response);
        assertEquals(2, response.getData().size());
        assertFalse(response.getMeta().isHasMore());

        // Assert rank and details of stickers
        StickerRankingResponse rankingResponse1 = response.getData().get(0);
        StickerRankingResponse rankingResponse2 = response.getData().get(1);

        assertEquals(1, rankingResponse1.getRank());
        assertEquals("1", rankingResponse1.getId());
        assertEquals(100, rankingResponse1.getHitCnt());

        assertEquals(2, rankingResponse2.getRank());
        assertEquals("2", rankingResponse2.getId());
        assertEquals(50, rankingResponse2.getHitCnt());
    }
}

