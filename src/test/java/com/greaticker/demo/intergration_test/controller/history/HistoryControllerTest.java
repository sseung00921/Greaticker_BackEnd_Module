package com.greaticker.demo.intergration_test.controller.history;

import com.greaticker.demo.dto.request.common.PaginationParam;
import com.greaticker.demo.dto.response.common.CursorPagination;
import com.greaticker.demo.dto.response.common.CursorPaginationMeta;
import com.greaticker.demo.dto.response.history.HistoryResponse;
import com.greaticker.demo.service.history.HistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class HistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HistoryService historyService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testShowHistoryOfUser() throws Exception {
        // Given
        CursorPaginationMeta meta = new CursorPaginationMeta(10, false);
        CursorPagination<HistoryResponse> pagination = new CursorPagination<>(meta, Collections.emptyList());

        when(historyService.showHistoryOfUser(any(PaginationParam.class))).thenReturn(pagination);

        // When & Then
        mockMvc.perform(get("/history/")
                        .param("count", "10")
                        .param("after", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess", is(true)))
                .andExpect(jsonPath("$.data.meta.count", is(10)))
                .andExpect(jsonPath("$.data.meta.hasMore", is(false)));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testShowHistoryOfUser_NoAfterParam() throws Exception {
        // Given
        CursorPaginationMeta meta = new CursorPaginationMeta(10, false);
        CursorPagination<HistoryResponse> pagination = new CursorPagination<>(meta, Collections.emptyList());

        when(historyService.showHistoryOfUser(any(PaginationParam.class))).thenReturn(pagination);

        // When & Then
        mockMvc.perform(get("/history/")
                        .param("count", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess", is(true)))
                .andExpect(jsonPath("$.data.meta.count", is(10)))
                .andExpect(jsonPath("$.data.meta.hasMore", is(false)));
    }
}

