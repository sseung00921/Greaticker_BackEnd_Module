package com.greaticker.demo.intergration_test.auth;

import com.greaticker.demo.dto.request.common.PaginationParam;
import com.greaticker.demo.dto.response.common.CursorPagination;
import com.greaticker.demo.dto.response.common.CursorPaginationMeta;
import com.greaticker.demo.dto.response.history.HistoryResponse;
import com.greaticker.demo.service.history.HistoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HistoryService historyService;

    @Test
    void canNotAccessWithoutLogin() throws Exception {
        // Given
        CursorPaginationMeta meta = new CursorPaginationMeta(10, false);
        CursorPagination<HistoryResponse> pagination = new CursorPagination<>(meta, Collections.emptyList());

        when(historyService.showHistoryOfUser(any(PaginationParam.class))).thenReturn(pagination);

        // When & Then
        mockMvc.perform(get("/history/")
                        .param("count", "10")
                        .param("after", "1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void canAccessWhenLoggedIn() throws Exception {
        // Given
        CursorPaginationMeta meta = new CursorPaginationMeta(10, false);
        CursorPagination<HistoryResponse> pagination = new CursorPagination<>(meta, Collections.emptyList());

        when(historyService.showHistoryOfUser(any(PaginationParam.class))).thenReturn(pagination);

        // When & Then
        mockMvc.perform(get("/history/")
                        .param("count", "10")
                        .param("after", "1"))
                .andExpect(status().isOk());
    }
}
