package com.greaticker.demo.controller.history;

import com.greaticker.demo.dto.request.PaginationParam;
import com.greaticker.demo.dto.response.common.ApiResponse;
import com.greaticker.demo.dto.response.common.CursorPagination;
import com.greaticker.demo.dto.response.history.HistoryResponseDto;
import com.greaticker.demo.service.history.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HistoryController {

    final HistoryService historyService;

    @GetMapping("/history/")
    public ApiResponse<CursorPagination<HistoryResponseDto>> showHistoryOfUser(@RequestParam int count, @RequestParam(required = false) Long after) {
        CursorPagination<HistoryResponseDto> fetchedData = historyService.showHistoryOfUser(new PaginationParam(count, after));
        return new ApiResponse<>(true, null, fetchedData);
    }

}
