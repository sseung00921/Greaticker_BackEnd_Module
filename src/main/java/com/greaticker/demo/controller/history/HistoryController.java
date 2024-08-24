package com.greaticker.demo.controller.history;

import com.greaticker.demo.dto.response.common.CursorPaginationDto;
import com.greaticker.demo.dto.response.history.HistoryResponseDto;
import com.greaticker.demo.service.history.HistoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class HistoryController {

    final HistoryService historyService;

    @GetMapping("/history/")
    public CursorPaginationDto<HistoryResponseDto> showHistoryOfUser() {
        return historyService.showHistoryOfUser();
    }

}
