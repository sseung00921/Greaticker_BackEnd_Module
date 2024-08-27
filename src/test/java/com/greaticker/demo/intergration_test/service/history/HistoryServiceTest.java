package com.greaticker.demo.intergration_test.service.history;

import com.greaticker.demo.dto.request.common.PaginationParam;
import com.greaticker.demo.dto.response.common.CursorPagination;
import com.greaticker.demo.dto.response.history.HistoryResponse;
import com.greaticker.demo.model.history.History;
import com.greaticker.demo.repository.history.HistoryRepository;
import com.greaticker.demo.service.history.HistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static com.greaticker.demo.constants.pagination.PaginationConstant.DEFAULT_FETCH_COUNT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
public class HistoryServiceTest {

    @Mock
    private HistoryRepository historyRepository;

    @InjectMocks
    private HistoryService historyService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testShowHistoryOfUser_WhenHasMore() {
        // Given
        PaginationParam paginationParam = new PaginationParam();
        paginationParam.setCount(DEFAULT_FETCH_COUNT);
        paginationParam.setAfter(null);

        // Mocking the repository call
        History history1 = new History(); // You may want to set fields here for more realistic testing
        History history2 = new History();
        History history3 = new History();
        History history4 = new History();
        History history5 = new History();
        History history6 = new History();
        History history7 = new History();
        History history8 = new History();
        History history9 = new History();
        History history10 = new History();
        History history11 = new History(); // 11th record to test hasMore

        List<History> mockHistories = Arrays.asList(
                history1, history2, history3, history4, history5,
                history6, history7, history8, history9, history10, history11);

        when(historyRepository.findHistoriesAfter(1L, paginationParam)).thenReturn(mockHistories);

        // When
        CursorPagination<HistoryResponse> result = historyService.showHistoryOfUser(paginationParam);

        // Then
        assertThat(result.getMeta().isHasMore()).isTrue();
        assertThat(result.getData()).hasSize(DEFAULT_FETCH_COUNT);
    }

    @Test
    public void testShowHistoryOfUser_WhenHasNoMore() {
        // Given
        PaginationParam paginationParam = new PaginationParam();
        paginationParam.setCount(DEFAULT_FETCH_COUNT);
        paginationParam.setAfter(null);

        // Mocking the repository call
        History history1 = new History();
        History history2 = new History();
        History history3 = new History();

        List<History> mockHistories = Arrays.asList(history1, history2, history3);

        when(historyRepository.findHistoriesAfter(1L, paginationParam)).thenReturn(mockHistories);

        // When
        CursorPagination<HistoryResponse> result = historyService.showHistoryOfUser(paginationParam);

        // Then
        assertThat(result.getMeta().isHasMore()).isFalse();
        assertThat(result.getData()).hasSize(mockHistories.size());
    }
}
