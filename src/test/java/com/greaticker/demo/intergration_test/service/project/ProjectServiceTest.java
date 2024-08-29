package com.greaticker.demo.intergration_test.service.project;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greaticker.demo.constants.enums.project.ProjectState;
import com.greaticker.demo.dto.request.project.ProjectRequest;
import com.greaticker.demo.dto.response.project.ProjectResponse;
import com.greaticker.demo.exception.customException.NoSupportedProjectStateChangeException;
import com.greaticker.demo.exception.customException.TodayStickerAlreadyGotException;
import com.greaticker.demo.exception.customException.TooLongProjectNameException;
import com.greaticker.demo.exception.customException.TooShortProjectNameException;
import com.greaticker.demo.model.history.History;
import com.greaticker.demo.model.project.Project;
import com.greaticker.demo.model.user.User;
import com.greaticker.demo.repository.history.HistoryRepository;
import com.greaticker.demo.repository.project.ProjectRepository;
import com.greaticker.demo.repository.user.UserRepository;
import com.greaticker.demo.service.project.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.greaticker.demo.constants.StickerCnt.TOTAL_STICKER_CNT;
import static com.greaticker.demo.constants.StringLimit.PROJECT_NAME_LENGTH_LIMIT;
import static com.greaticker.demo.constants.StringLimit.PROJECT_NAME_LENGTH_UNDER_LIMIT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class ProjectServiceTest {

    @MockBean
    private ProjectRepository projectRepository;

    @MockBean
    private HistoryRepository historyRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProjectService projectService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setStickerInventory("[]");
    }

    @Test
    void testGetNewStickerSuccess() throws JsonProcessingException {
        // Arrange
        user.setLastGet(LocalDateTime.now().minusDays(1));
        user.setNowProjectId(20L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setDay_in_a_row(0);
        when(projectRepository.findById(user.getNowProjectId())).thenReturn(Optional.of(project));

        // Act
        String gotStickerId = projectService.getNewSticker();

        // Assert
        assertNotNull(gotStickerId);
        verify(userRepository).findById(1L);
        verify(historyRepository).save(any(History.class));
    }

    @Test
    void testGetNewStickerAlreadyGotToday() {
        // Arrange
        user.setLastGet(LocalDateTime.now());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(TodayStickerAlreadyGotException.class, () -> projectService.getNewSticker());
    }

    @Test
    void testUpdateProjectStartNewProject() {
        // Arrange
        ProjectRequest request = new ProjectRequest();
        request.setPrevProjectState(ProjectState.NO_EXIST);
        request.setNextProjectState(ProjectState.IN_PROGRESS);
        request.setProjectName("New Project");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Project newProject = new Project();
        newProject.setId(1L);
        newProject.setName("New Project");
        newProject.setState(ProjectState.IN_PROGRESS);
        when(projectRepository.save(any(Project.class))).thenReturn(newProject);

        // Act
        String projectId = projectService.updateProject(request);

        // Assert
        assertEquals("1", projectId);
        verify(projectRepository).save(any(Project.class));
        verify(historyRepository).save(any(History.class));
    }

    @Test
    void testUpdateProjectCompleteProject() {
        // Arrange
        ProjectRequest request = new ProjectRequest();
        request.setPrevProjectState(ProjectState.IN_PROGRESS);
        request.setNextProjectState(ProjectState.COMPLETED);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Project existingProject = new Project();
        existingProject.setId(1L);
        existingProject.setName("Existing Project");
        existingProject.setState(ProjectState.IN_PROGRESS);
        when(projectRepository.findById(user.getNowProjectId())).thenReturn(Optional.of(existingProject));

        // Act
        String projectId = projectService.updateProject(request);

        // Assert
        assertEquals("1", projectId);
        verify(projectRepository).findById(user.getNowProjectId());
        verify(historyRepository).save(any(History.class));
    }

    @Test
    void testUpdateProjectDeleteProject() {
        // Arrange
        ProjectRequest request = new ProjectRequest();
        request.setPrevProjectState(ProjectState.IN_PROGRESS);
        request.setNextProjectState(ProjectState.NO_EXIST);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Project existingProject = new Project();
        existingProject.setId(1L);
        existingProject.setName("Existing Project");
        existingProject.setState(ProjectState.IN_PROGRESS);
        when(projectRepository.findById(user.getNowProjectId())).thenReturn(Optional.of(existingProject));

        // Act
        String projectId = projectService.updateProject(request);

        // Assert
        assertEquals("1", projectId);
        verify(projectRepository).delete(existingProject);
        verify(historyRepository).save(any(History.class));
    }

    @Test
    void testUpdateProjectInvalidStateChange() {
        // Arrange
        ProjectRequest request = new ProjectRequest();
        request.setPrevProjectState(ProjectState.NO_EXIST);
        request.setNextProjectState(ProjectState.COMPLETED);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(NoSupportedProjectStateChangeException.class, () -> projectService.updateProject(request));
    }

    @Test
    void testGetProject() {
        // Arrange
        user.setNowProjectId(20L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Project existingProject = new Project();
        existingProject.setId(20L);
        existingProject.setName("Existing Project");
        existingProject.setState(ProjectState.IN_PROGRESS);
        when(projectRepository.findById(user.getNowProjectId())).thenReturn(Optional.of(existingProject));

        // Act
        ProjectResponse response = projectService.getProject();

        // Assert
        assertEquals(ProjectState.IN_PROGRESS, response.getProjectStateKind());
        assertEquals("Existing Project", response.getProjectName());
        verify(projectRepository).findById(existingProject.getId());
    }

    @Test
    void testGetProjectWhenProjectIsNoExist() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        user.setNowProjectId(20L);
        when(projectRepository.findById(user.getNowProjectId())).thenReturn(Optional.empty());

        // Act
        ProjectResponse response = projectService.getProject();

        // Assert
        assertEquals(ProjectState.NO_EXIST, response.getProjectStateKind());
        assertNull(response.getProjectName());
        verify(projectRepository).findById(user.getNowProjectId());
    }

    @Test
    void testCheckNamingRuleTooLongName() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        String longName = "A".repeat((PROJECT_NAME_LENGTH_LIMIT/3) + 1);

        // Act & Assert
        assertThrows(TooLongProjectNameException.class, () -> projectService.updateProject(new ProjectRequest(null, longName, ProjectState.NO_EXIST, ProjectState.IN_PROGRESS)));

    }

    @Test
    void testCheckNamingRuleTooShortName() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        String shortName = "A".repeat((PROJECT_NAME_LENGTH_UNDER_LIMIT/3) - 1);

        // Act & Assert
        assertThrows(TooShortProjectNameException.class, () -> projectService.updateProject(new ProjectRequest(null, shortName, ProjectState.NO_EXIST, ProjectState.IN_PROGRESS)));
    }

    @Test
    void testGetRandomStickerWhenStickerInventoryIsFull() throws JsonProcessingException {
        // Arrange
        List<String> fullStickerInventory = new ArrayList<>();
        for (int i = 1; i <= TOTAL_STICKER_CNT; i++) {
            fullStickerInventory.add(String.valueOf(i));
        }
        String fullStickerInventoryStr = objectMapper.writeValueAsString(fullStickerInventory);
        User userHavingAllSticker = new User();
        userHavingAllSticker.setId(1L);
        userHavingAllSticker.setStickerInventory(fullStickerInventoryStr);
        when(userRepository.findById(1L)).thenReturn(Optional.of(userHavingAllSticker));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> projectService.getNewSticker());
    }
}
