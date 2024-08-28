package com.greaticker.demo.controller.hallOfFame;

import com.greaticker.demo.dto.request.common.PaginationParam;
import com.greaticker.demo.dto.request.hallOfFame.HallOfFameDeleteRequest;
import com.greaticker.demo.dto.request.hallOfFame.HallOfFameRegisterRequest;
import com.greaticker.demo.dto.request.hallOfFame.HitGoodToProjectRequest;
import com.greaticker.demo.dto.response.common.ApiResponse;
import com.greaticker.demo.dto.response.common.CursorPagination;
import com.greaticker.demo.dto.response.hallOfFame.HallOfFamePostApiResponse;
import com.greaticker.demo.dto.response.hallOfFame.HallOfFameResponse;
import com.greaticker.demo.dto.response.history.HistoryResponse;
import com.greaticker.demo.service.hallOfFame.HallOfFameService;
import com.greaticker.demo.service.history.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("hall-of-fame")
public class HallOfFameController {

    final HallOfFameService hallOfFameService;

//    @GetMapping("/")
//    public ResponseEntity<ApiResponse<CursorPagination<HallOfFameResponse>>> showHallOfFame(@RequestParam int count, @RequestParam(required = false) Long after) {
//        CursorPagination<HallOfFameResponse> fetchedData = hallOfFameService.showHallOfFame(new PaginationParam(count, after));
//        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, null, fetchedData));
//    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<HallOfFamePostApiResponse>> registerHallOfFame(@RequestBody HallOfFameRegisterRequest hallOfFameRegisterRequest) {
        HallOfFamePostApiResponse fetchedData = hallOfFameService.registerHallOfFame(hallOfFameRegisterRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, null, fetchedData));
    }

    @PostMapping("/delete")
    public ResponseEntity<ApiResponse<HallOfFamePostApiResponse>> deleteHallOfFame(@RequestBody HallOfFameDeleteRequest hallOfFameDeleteRequest) throws IllegalAccessException {
        HallOfFamePostApiResponse fetchedData = hallOfFameService.deleteHallOfFame(hallOfFameDeleteRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, null, fetchedData));
    }

    @PostMapping("/hit-good")
    public ResponseEntity<ApiResponse<HallOfFamePostApiResponse>> hitGoodToHallOfFame(@RequestBody HitGoodToProjectRequest hitGoodToProjectRequest) throws IllegalAccessException {
        HallOfFamePostApiResponse fetchedData = hallOfFameService.hitGoodToHallOfFame(hitGoodToProjectRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, null, fetchedData));
    }

}
