package com.greaticker.demo.controller.project;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.greaticker.demo.dto.request.common.PaginationParam;
import com.greaticker.demo.dto.request.project.ProjectRequest;
import com.greaticker.demo.dto.response.common.ApiResponse;
import com.greaticker.demo.dto.response.project.ProjectResponse;
import com.greaticker.demo.service.project.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("home")
public class ProjectController {

    final ProjectService projectService;

    @GetMapping("/project")
    public ResponseEntity<ApiResponse<ProjectResponse>> getProject() {
        ProjectResponse fetchedData = projectService.getProject();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, null, fetchedData));
    }

    @PostMapping("/project")
    public ResponseEntity<ApiResponse<String>> updateProject(@RequestBody ProjectRequest projectRequest) {
        String fetchedData = projectService.updateProject(projectRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, null, fetchedData));
    }

    @PostMapping("/get-sticker")
    public ResponseEntity<ApiResponse<String>> getNewSticker() throws JsonProcessingException {
        String fetchedStickerId = projectService.getNewSticker();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, null, fetchedStickerId));
    }

}
