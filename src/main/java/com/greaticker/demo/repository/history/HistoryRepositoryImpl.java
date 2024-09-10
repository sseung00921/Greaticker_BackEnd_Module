package com.greaticker.demo.repository.history;

import com.greaticker.demo.dto.request.common.PaginationParam;
import com.greaticker.demo.model.history.History;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.greaticker.demo.model.history.QHistory.history;

@Repository
@RequiredArgsConstructor
public class HistoryRepositoryImpl implements HistoryRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<History> findHistoriesAfter(Long userId, PaginationParam paginationParam) {
        Long after = paginationParam.getAfter();
        int count = paginationParam.getCount();
        return queryFactory
                .selectFrom(history)
                .where(history.user.id.eq(userId)
                        .and(after != null ? history.id.lt(after) : null))
                .orderBy(history.id.desc())
                .limit(count + 1)
                .fetch();
    }
}
