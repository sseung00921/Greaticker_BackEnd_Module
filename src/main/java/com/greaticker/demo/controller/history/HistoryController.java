package com.greaticker.demo.controller.history;

import com.greaticker.demo.dto.request.common.PaginationParam;
import com.greaticker.demo.dto.response.common.ApiResponse;
import com.greaticker.demo.dto.response.common.CursorPagination;
import com.greaticker.demo.dto.response.history.HistoryResponse;
import com.greaticker.demo.service.history.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HistoryController {

    final HistoryService historyService;

    @GetMapping("/history/")
    public ResponseEntity<ApiResponse<CursorPagination<HistoryResponse>>> showHistoryOfUser(@RequestParam int count, @RequestParam(required = false) Long after) {
        CursorPagination<HistoryResponse> fetchedData = historyService.showHistoryOfUser(new PaginationParam(count, after));
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, null, fetchedData));
    }

}
