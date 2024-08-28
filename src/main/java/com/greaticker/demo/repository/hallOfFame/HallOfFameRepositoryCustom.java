package com.greaticker.demo.repository.hallOfFame;

import com.greaticker.demo.dto.request.common.PaginationParam;
import com.greaticker.demo.model.hallOfFame.HallOfFame;
import com.greaticker.demo.model.history.History;

import java.util.List;

public interface HallOfFameRepositoryCustom {
    List<HallOfFame> findHallOfFameAfter(PaginationParam paginationParam);
}
