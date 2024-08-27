package com.greaticker.demo.dto.request.project;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.greaticker.demo.constants.enums.project.ProjectState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectRequest {
    private Long projectId;
    private String projectName;
    private ProjectState prevProjectState;
    private ProjectState nextProjectState;
}
