package com.greaticker.demo.unit_test.dto.response;

import com.greaticker.demo.constants.enums.history.HistoryKind;
import com.greaticker.demo.dto.response.history.HistoryResponse;
import com.greaticker.demo.model.history.History;
import com.greaticker.demo.utils.StringConverter;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class HistoryResponseDtoTest {

    @Test
    public void testFromEntity() {
        // Given
        Long id = 1L;
        HistoryKind kind = HistoryKind.GET_STICKER;
        String projectName = "Test Project";
        Long stickerId = 10L;
        Integer dayInARow = 5;
        LocalDateTime createdDateTime = LocalDateTime.of(2024, 8, 24, 10, 0, 0);
        LocalDateTime updatedDateTime = LocalDateTime.of(2024, 8, 24, 10, 0, 0);

        History history = new History();
        history.setId(id);
        history.setKind(kind);
        history.setProject_name(projectName);
        history.setSticker_id(stickerId);
        history.setDay_in_a_row(dayInARow);
        history.setCreatedDateTime(createdDateTime);
        history.setUpdatedDateTime(updatedDateTime);

        // When
        HistoryResponse dto = HistoryResponse.fromEntity(history);

        // Then
        assertThat(dto.getId()).isEqualTo(StringConverter.longToStringConvert(id));
        assertThat(dto.getHistoryKind()).isEqualTo(kind);
        assertThat(dto.getProjectName()).isEqualTo(projectName);
        assertThat(dto.getStickerId()).isEqualTo(StringConverter.longToStringConvert(stickerId));
        assertThat(dto.getDayInARow()).isEqualTo(dayInARow);
        assertThat(dto.getCreatedDateTime()).isEqualTo(createdDateTime);
        assertThat(dto.getUpdatedDateTime()).isEqualTo(updatedDateTime);
    }
}

