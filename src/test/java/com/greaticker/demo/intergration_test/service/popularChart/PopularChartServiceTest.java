package com.greaticker.demo.intergration_test.service.popularChart;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.greaticker.demo.dto.response.common.CursorPagination;
import com.greaticker.demo.dto.response.sticker.StickerRankingResponse;
import com.greaticker.demo.model.sticker.Sticker;
import com.greaticker.demo.model.user.User;
import com.greaticker.demo.repository.sticker.StickerRepository;
import com.greaticker.demo.repository.user.UserRepository;
import com.greaticker.demo.service.popularChart.PopularChartService;
import com.greaticker.demo.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class PopularChartServiceTest {

    @MockBean
    private UserService userService;

    @MockBean
    private StickerRepository stickerRepository;

    @Autowired
    private PopularChartService popularChartService;

    private User user;
    private Sticker sticker1;
    private Sticker sticker2;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setStickerInventory("[]");
        user.setHitFavoriteList("[]");

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
    void testShowStickerRanking() throws JsonProcessingException {
        // Arrange
        user.setStickerInventory("[\"1\", \"2\"]");
        when(userService.getCurrentUser()).thenReturn(user);
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

    @Test
    void testHideStickersUserNotHaveInRanking() throws JsonProcessingException {
        // Arrange
        user.setStickerInventory("[\"2\"]");
        when(userService.getCurrentUser()).thenReturn(user);
        List<Sticker> stickers = Arrays.asList(sticker1, sticker2);
        when(stickerRepository.findAllByOrderByHitCntDesc()).thenReturn(stickers);

        // Act
        CursorPagination<StickerRankingResponse> response = popularChartService.showStickerRanking();

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getData().size());
        assertFalse(response.getMeta().isHasMore());

        // Assert rank and details of stickers
        StickerRankingResponse rankingResponse1 = response.getData().get(0);

        assertEquals(2, rankingResponse1.getRank());
        assertEquals("2", rankingResponse1.getId());
        assertEquals(50, rankingResponse1.getHitCnt());
    }
}

