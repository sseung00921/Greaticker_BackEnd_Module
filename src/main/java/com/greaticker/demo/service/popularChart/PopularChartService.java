package com.greaticker.demo.service.popularChart;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greaticker.demo.dto.response.common.CursorPagination;
import com.greaticker.demo.dto.response.common.CursorPaginationMeta;
import com.greaticker.demo.dto.response.sticker.StickerRankingResponse;
import com.greaticker.demo.model.sticker.Sticker;
import com.greaticker.demo.model.user.User;
import com.greaticker.demo.repository.sticker.StickerRepository;
import com.greaticker.demo.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.greaticker.demo.constants.StickerCnt.TOTAL_STICKER_CNT;


@Service
@Transactional
@RequiredArgsConstructor
public class PopularChartService {

    private final UserRepository userRepository;
    private final StickerRepository stickerRepository;
    private final ObjectMapper objectMapper;

    public CursorPagination<StickerRankingResponse> showStickerRanking() throws JsonProcessingException {
        List<Sticker> fetchedData = stickerRepository.findAllByOrderByHitCntDesc();

        AtomicInteger index = new AtomicInteger(1);
        List<StickerRankingResponse> stickerResponseList = fetchedData.stream()
                .map(entity -> {
                    StickerRankingResponse response = StickerRankingResponse.fromEntity(entity);
                    response.setRank(index.getAndIncrement());
                    return response;
                })
                .toList();

        User user = userRepository.findById(1L).get();
        List<String> stickerInventoryList = objectMapper.readValue(user.getStickerInventory(), new TypeReference<List<String>>() {});
        stickerResponseList = stickerResponseList.stream().filter(e -> stickerInventoryList.contains(e.getId())).collect(Collectors.toList());
        for (StickerRankingResponse e : stickerResponseList){
            System.out.println(e.getId());
        }
        return new CursorPagination<>(new CursorPaginationMeta(TOTAL_STICKER_CNT, false), stickerResponseList);
    }
}
