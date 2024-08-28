package com.greaticker.demo.service.popularChart;

import com.greaticker.demo.dto.response.common.CursorPagination;
import com.greaticker.demo.dto.response.common.CursorPaginationMeta;
import com.greaticker.demo.dto.response.sticker.StickerRankingResponse;
import com.greaticker.demo.model.sticker.Sticker;
import com.greaticker.demo.repository.sticker.StickerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.greaticker.demo.constants.StickerCnt.TOTAL_STICKER_CNT;


@Service
@Transactional
@RequiredArgsConstructor
public class PopularChartService {

    private final StickerRepository stickerRepository;

    public CursorPagination<StickerRankingResponse> showStickerRanking() {
        List<Sticker> fetchedData = stickerRepository.findAllByOrderByHitCntDesc();

        AtomicInteger index = new AtomicInteger(1);
        List<StickerRankingResponse> stickerResponseList = fetchedData.stream()
                .map(entity -> {
                    StickerRankingResponse response = StickerRankingResponse.fromEntity(entity);
                    response.setRank(index.getAndIncrement());
                    return response;
                })
                .toList();

        return new CursorPagination<>(new CursorPaginationMeta(TOTAL_STICKER_CNT, false), stickerResponseList);
    }
}
