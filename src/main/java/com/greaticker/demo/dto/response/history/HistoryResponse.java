package com.greaticker.demo.dto.response.history;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.greaticker.demo.constants.enums.history.HistoryKind;
import com.greaticker.demo.model.history.History;
import com.greaticker.demo.utils.StringConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HistoryResponse {
    private String id;
    private HistoryKind historyKind;
    private String projectName;
    private String stickerId;
    private Integer dayInARow;
    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;

    public static HistoryResponse fromEntity(History entity) {
        return new HistoryResponse(StringConverter.longToStringConvert(entity.getId()), entity.getKind(), entity.getProject_name(),
                StringConverter.longToStringConvert(entity.getSticker_id()), entity.getDay_in_a_row(), entity.getCreatedDateTime(), entity.getUpdatedDateTime());
    }
}
