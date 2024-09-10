package com.greaticker.demo.service.diary;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greaticker.demo.constants.StickerCnt;
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
import com.greaticker.demo.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.greaticker.demo.constants.StickerCnt.FAVORITE_LIMIT_CNT;
import static com.greaticker.demo.constants.StickerCnt.TOTAL_STICKER_CNT;
import static com.greaticker.demo.exception.errorCode.ErrorCode.CAN_NOT_HIT_FAVORITE_BEFORE_GOT_ALL_STICKER;
import static com.greaticker.demo.exception.errorCode.ErrorCode.OVER_HIT_FAVORITE_LIMIT;

@Service
@Transactional
@RequiredArgsConstructor
public class DiaryService {

    final UserService userService;
    final StickerRepository stickerRepository;
    final ObjectMapper objectMapper;

    public DiaryResponse getDiaryModel() throws JsonProcessingException {
        User user = userService.getCurrentUser();
        return DiaryResponse.fromEntity(user);
    }

    public void updateDiaryModel(DiaryRequest diaryRequest) throws JsonProcessingException {
        User user = userService.getCurrentUser();
        user.setStickerInventory(objectMapper.writeValueAsString(diaryRequest.getStickerInventory()));
    }


    public HitFavoriteStickerResponse hitFavorite(HitFavoriteToStickerRequest hitFavoriteToStickerReqeust) throws IllegalAccessException, JsonProcessingException {
        User user = userService.getCurrentUser();
        String userHitFavoriteList = user.getHitFavoriteList();
        Set<String> userHitFavoriteListSet = objectMapper.readValue(userHitFavoriteList, new TypeReference<Set<String>>() {
        });
        String stickerInveontory = user.getStickerInventory();
        List<String> stickerInveontoryList = objectMapper.readValue(stickerInveontory, new TypeReference<List<String>>() {
        });
        if (stickerInveontoryList.size() < TOTAL_STICKER_CNT) {
            throw new CanNotHitFavoriteBeforeGotAllStickerException(CAN_NOT_HIT_FAVORITE_BEFORE_GOT_ALL_STICKER);
        }

        String requestedStickerIdStr = hitFavoriteToStickerReqeust.getStickerId();
        Long requestedStickerId = Long.valueOf(requestedStickerIdStr);
        Optional<Sticker> fetchedResponse = stickerRepository.findById(requestedStickerId);
        if (fetchedResponse.isEmpty()) {
            throw new IllegalAccessException("Sticker can not be null since favorite icon was hit");
        }
        Sticker reqeustedSticker = fetchedResponse.get();
        if (userHitFavoriteListSet.contains(requestedStickerIdStr)) {
            userHitFavoriteListSet.remove(requestedStickerIdStr);
            user.setHitFavoriteList(objectMapper.writeValueAsString(userHitFavoriteListSet));
            reqeustedSticker.minusHit();
        } else {
            if (userHitFavoriteListSet.size() == FAVORITE_LIMIT_CNT) {
                throw new OverHitFavoriteLimitException(OVER_HIT_FAVORITE_LIMIT);
            }
            userHitFavoriteListSet.add(requestedStickerIdStr);
            user.setHitFavoriteList(objectMapper.writeValueAsString(userHitFavoriteListSet));
            reqeustedSticker.plusHit();
        }

        return HitFavoriteStickerResponse.fromEntity(reqeustedSticker);
    }
}
