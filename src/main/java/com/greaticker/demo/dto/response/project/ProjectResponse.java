package com.greaticker.demo.dto.response.project;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.greaticker.demo.constants.enums.project.ProjectState;
import com.greaticker.demo.dto.response.history.HistoryResponse;
import com.greaticker.demo.model.history.History;
import com.greaticker.demo.model.project.Project;
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
public class ProjectResponse {
    private ProjectState projectStateKind;
    private String projectName;
    private LocalDateTime startDate;
    private Integer dayInARow;

    public static ProjectResponse fromEntity(Project entity) {
        return new ProjectResponse(entity.getState(), entity.getName(), entity.getStart_date(), entity.getDay_in_a_row());
    }
}
