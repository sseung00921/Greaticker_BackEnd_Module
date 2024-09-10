package com.greaticker.demo.controller.diary;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.greaticker.demo.dto.request.common.PaginationParam;
import com.greaticker.demo.dto.request.diary.DiaryRequest;
import com.greaticker.demo.dto.request.diary.HitFavoriteToStickerRequest;
import com.greaticker.demo.dto.response.common.ApiResponse;
import com.greaticker.demo.dto.response.diary.DiaryResponse;
import com.greaticker.demo.dto.response.sticker.HitFavoriteStickerResponse;
import com.greaticker.demo.service.diary.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("diary")
public class DiaryController {

    final DiaryService diaryService;

    @GetMapping("/")
    public ResponseEntity<ApiResponse<DiaryResponse>> getDiaryModel() throws JsonProcessingException {
        DiaryResponse fetchedData = diaryService.getDiaryModel();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, null, fetchedData));
    }

    @PostMapping("/re-order")
    public ResponseEntity<ApiResponse<String>> updateDiaryModel(@RequestBody DiaryRequest diaryRequest) throws JsonProcessingException {
        diaryService.updateDiaryModel(diaryRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, null, null));
    }

    @PostMapping("/hit-favorite")
    public ResponseEntity<ApiResponse<HitFavoriteStickerResponse>> hitFavorite(@RequestBody HitFavoriteToStickerRequest hitFavoriteToStickerReqeust) throws IllegalAccessException, JsonProcessingException {
        HitFavoriteStickerResponse requestedSticker = diaryService.hitFavorite(hitFavoriteToStickerReqeust);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, null, requestedSticker));
    }

}
