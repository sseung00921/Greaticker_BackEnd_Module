package com.greaticker.demo.intergration_test.service.diary;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greaticker.demo.dto.request.diary.DiaryRequest;
import com.greaticker.demo.dto.request.diary.HitFavoriteToStickerRequest;
import com.greaticker.demo.dto.response.diary.DiaryResponse;
import com.greaticker.demo.dto.response.sticker.HitFavoriteStickerResponse;
import com.greaticker.demo.exception.customException.CanNotHitFavoriteBeforeGotAllStickerException;
import com.greaticker.demo.exception.customException.OverHitFavoriteLimitException;
import com.greaticker.demo.model.sticker.Sticker;
import com.greaticker.demo.model.user.User;
import com.greaticker.demo.repository.sticker.StickerRepository;
import com.greaticker.demo.repository.user.UserRepository;
import com.greaticker.demo.service.diary.DiaryService;
import com.greaticker.demo.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.greaticker.demo.constants.StickerCnt.FAVORITE_LIMIT_CNT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class DiaryServiceTest {

    @MockBean
    private UserService userService;

    @MockBean
    private StickerRepository stickerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DiaryService diaryService;

    private User user;
    private Sticker sticker;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setStickerInventory("[]");
        user.setHitFavoriteList("[]");

        sticker = new Sticker();
        sticker.setId(2L);
        sticker.setHitCnt(0);
    }

    @Test
    void testGetDiaryModel() throws JsonProcessingException {
        // Arrange
        when(userService.getCurrentUser()).thenReturn(user);

        // Act
        DiaryResponse response = diaryService.getDiaryModel();

        // Assert
        assertNotNull(response);
        verify(userService).getCurrentUser();
    }

    @Test
    void testUpdateDiaryModel() throws JsonProcessingException {
        // Arrange
        DiaryRequest diaryRequest = new DiaryRequest();
        diaryRequest.setStickerInventory(Collections.singletonList("1"));
        when(userService.getCurrentUser()).thenReturn(user);

        // Act
        diaryService.updateDiaryModel(diaryRequest);

        // Assert
        assertEquals("[\"1\"]", user.getStickerInventory());
        verify(userService).getCurrentUser();
    }

    @Test
    void testHitFavoriteSuccess() throws JsonProcessingException, IllegalAccessException {
        // Arrange
        user.setStickerInventory("[\"1\", \"2\", \"3\", \"4\", \"5\", \"6\", \"7\", \"8\", \"9\", \"10\", \"11\", \"12\", \"13\", \"14\", \"15\", \"16\", \"17\", \"18\", \"19\", \"20\", \"21\", \"22\", \"23\", \"24\"]");
        when(userService.getCurrentUser()).thenReturn(user);
        when(stickerRepository.findById(1L)).thenReturn(Optional.of(sticker));

        HitFavoriteToStickerRequest request = new HitFavoriteToStickerRequest();
        request.setStickerId("1");

        // Act
        HitFavoriteStickerResponse response = diaryService.hitFavorite(request);


        // Assert
        assertNotNull(response);
        assertEquals(1, sticker.getHitCnt());
        assertTrue(objectMapper.readValue(user.getHitFavoriteList(), new TypeReference<Set<String>>() {
        }).contains("1"));
        verify(stickerRepository).findById(1L);
    }

    @Test
    void testHitFavoriteRemoveFavorite() throws JsonProcessingException, IllegalAccessException {
        // Arrange
        user.setStickerInventory("[\"1\", \"2\", \"3\", \"4\", \"5\", \"6\", \"7\", \"8\", \"9\", \"10\", \"11\", \"12\", \"13\", \"14\", \"15\", \"16\", \"17\", \"18\", \"19\", \"20\", \"21\", \"22\", \"23\", \"24\"]");
        Set<String> favoriteSet = new HashSet<>();
        favoriteSet.add("1");
        user.setHitFavoriteList(objectMapper.writeValueAsString(favoriteSet));
        sticker.setHitCnt(1);
        when(userService.getCurrentUser()).thenReturn(user);
        when(stickerRepository.findById(1L)).thenReturn(Optional.of(sticker));

        HitFavoriteToStickerRequest request = new HitFavoriteToStickerRequest();
        request.setStickerId("1");

        // Act
        HitFavoriteStickerResponse response = diaryService.hitFavorite(request);

        // Assert
        assertNotNull(response);
        assertEquals(0, sticker.getHitCnt());
        assertFalse(objectMapper.readValue(user.getHitFavoriteList(), new TypeReference<Set<String>>() {
        }).contains("1"));
        verify(stickerRepository).findById(1L);
    }

    @Test
    void testHitFavoriteBeforeGotAllSticker() throws JsonProcessingException {
        // Arrange
        user.setStickerInventory("[]");
        when(userService.getCurrentUser()).thenReturn(user);

        HitFavoriteToStickerRequest request = new HitFavoriteToStickerRequest();
        request.setStickerId("1");

        // Act & Assert
        assertThrows(CanNotHitFavoriteBeforeGotAllStickerException.class, () -> diaryService.hitFavorite(request));
    }

    @Test
    void testHitFavoriteOverLimit() throws JsonProcessingException {
        // Arrange
        user.setStickerInventory("[\"1\", \"2\", \"3\", \"4\", \"5\", \"6\", \"7\", \"8\", \"9\", \"10\", \"11\", \"12\", \"13\", \"14\", \"15\", \"16\", \"17\", \"18\", \"19\", \"20\", \"21\", \"22\", \"23\", \"24\"]");
        Set<String> favoriteSet = new HashSet<>();
        for (int i = 1; i <= FAVORITE_LIMIT_CNT; i++) {
            favoriteSet.add(String.valueOf(i));
        }
        user.setHitFavoriteList(objectMapper.writeValueAsString(favoriteSet));
        sticker.setId(99L);
        when(userService.getCurrentUser()).thenReturn(user);
        when(stickerRepository.findById(99L)).thenReturn(Optional.of(sticker));

        HitFavoriteToStickerRequest request = new HitFavoriteToStickerRequest();
        request.setStickerId("99");

        // Act & Assert
        assertThrows(OverHitFavoriteLimitException.class, () -> diaryService.hitFavorite(request));
    }
}

