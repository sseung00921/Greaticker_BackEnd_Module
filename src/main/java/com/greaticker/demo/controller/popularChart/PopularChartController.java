package com.greaticker.demo.controller.popularChart;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.greaticker.demo.dto.response.common.ApiResponse;
import com.greaticker.demo.dto.response.common.CursorPagination;
import com.greaticker.demo.dto.response.sticker.StickerRankingResponse;
import com.greaticker.demo.service.popularChart.PopularChartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("popular-chart")
public class PopularChartController {

    final PopularChartService popularChartService;

    @GetMapping("/")
    public ResponseEntity<ApiResponse<CursorPagination<StickerRankingResponse>>> showStickerRanking() throws JsonProcessingException {
        CursorPagination<StickerRankingResponse> fetchedData = popularChartService.showStickerRanking();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, null, fetchedData));
    }

}
