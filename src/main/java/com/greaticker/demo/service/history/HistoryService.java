package com.greaticker.demo.service.history;

import com.greaticker.demo.dto.request.common.PaginationParam;
import com.greaticker.demo.dto.response.common.CursorPagination;
import com.greaticker.demo.dto.response.common.CursorPaginationMeta;
import com.greaticker.demo.dto.response.history.HistoryResponse;
import com.greaticker.demo.model.history.History;
import com.greaticker.demo.repository.history.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.greaticker.demo.constants.pagination.PaginationConstant.DEFAULT_FETCH_COUNT;


@Service
@Transactional
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;

    @Transactional(readOnly = true)
    public CursorPagination<HistoryResponse> showHistoryOfUser(PaginationParam paginationParam) {
        Long user_id = 1L; //추후 여기서 Redis에서 유저정보를 가져오게 수정할 거임
        List<History> fetchedData = historyRepository.findHistoriesAfter(user_id, paginationParam);

        boolean hasMore = fetchedData.size() > DEFAULT_FETCH_COUNT;
        if (hasMore) {
            fetchedData = fetchedData.subList(0, DEFAULT_FETCH_COUNT);
        }

        List<HistoryResponse> historyResponseDtoList = fetchedData.stream().map(HistoryResponse::fromEntity).toList();
        return new CursorPagination<>(new CursorPaginationMeta(DEFAULT_FETCH_COUNT, hasMore), historyResponseDtoList);
    }
}
