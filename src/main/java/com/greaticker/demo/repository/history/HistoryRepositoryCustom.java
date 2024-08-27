package com.greaticker.demo.repository.history;

import com.greaticker.demo.dto.request.common.PaginationParam;
import com.greaticker.demo.model.history.History;

import java.util.List;

public interface HistoryRepositoryCustom {
    List<History> findHistoriesAfter(Long userId, PaginationParam paginationParam);
}
