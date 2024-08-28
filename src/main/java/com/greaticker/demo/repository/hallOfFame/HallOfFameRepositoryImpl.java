package com.greaticker.demo.repository.hallOfFame;

import com.greaticker.demo.dto.request.common.PaginationParam;
import com.greaticker.demo.model.hallOfFame.HallOfFame;
import com.greaticker.demo.model.history.History;
import com.greaticker.demo.repository.history.HistoryRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.greaticker.demo.model.hallOfFame.QHallOfFame.hallOfFame;
import static com.greaticker.demo.model.history.QHistory.history;

@Repository
@RequiredArgsConstructor
public class HallOfFameRepositoryImpl implements HallOfFameRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<HallOfFame> findHallOfFameAfter(PaginationParam paginationParam) {
        Long after = paginationParam.getAfter();
        int count = paginationParam.getCount();
        return queryFactory
                .selectFrom(hallOfFame)
                .where(after != null ? hallOfFame.id.lt(after) : null)
                .orderBy(hallOfFame.id.desc())
                .limit(count + 1)
                .fetch();
    }
}
