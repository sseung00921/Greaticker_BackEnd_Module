package com.greaticker.demo.intergration_test.service.hallOfFame;

import com.greaticker.demo.dto.request.common.PaginationParam;
import com.greaticker.demo.dto.request.hallOfFame.HallOfFameDeleteRequest;
import com.greaticker.demo.dto.request.hallOfFame.HallOfFameRegisterRequest;
import com.greaticker.demo.dto.request.hallOfFame.HitGoodToProjectRequest;
import com.greaticker.demo.dto.response.common.CursorPagination;
import com.greaticker.demo.dto.response.hallOfFame.HallOfFamePostApiResponse;
import com.greaticker.demo.dto.response.hallOfFame.HallOfFameResponse;
import com.greaticker.demo.exception.customException.DuplicatedHallOfFameException;
import com.greaticker.demo.model.hallOfFame.HallOfFame;
import com.greaticker.demo.model.project.Project;
import com.greaticker.demo.model.relationShip.userHallOfFameHitCntRelationShip.UserHallOfFameHitCntRelationship;
import com.greaticker.demo.model.user.User;
import com.greaticker.demo.repository.hallOfFame.HallOfFameRepository;
import com.greaticker.demo.repository.project.ProjectRepository;
import com.greaticker.demo.repository.relationShip.userHallOfFameHitCntRelationship.UserHallOfFameHitCntRelationShipRepository;
import com.greaticker.demo.repository.user.UserRepository;
import com.greaticker.demo.service.hallOfFame.HallOfFameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static com.greaticker.demo.constants.pagination.PaginationConstant.DEFAULT_FETCH_COUNT;
import static com.greaticker.demo.exception.errorCode.ErrorCode.DUPLICATED_HALL_OF_FAME;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class HallOfFameServiceTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ProjectRepository projectRepository;

    @MockBean
    private HallOfFameRepository hallOfFameRepository;

    @MockBean
    private UserHallOfFameHitCntRelationShipRepository userHallOfFameHitCntRelationShipRepository;

    @Autowired
    private HallOfFameService hallOfFameService;

    private User user;
    private Project project;
    private HallOfFame hallOfFame;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setNowProjectId(1L);

        project = new Project();
        project.setId(1L);
        project.setName("Test Project");

        hallOfFame = new HallOfFame();
        hallOfFame.setId(1L);
        hallOfFame.setUser(user);
        hallOfFame.setProjectId(1L);
        hallOfFame.setHitCnt(0);
    }

    @Test
    void testShowHallOfFame() throws IllegalAccessException {
        // Arrange
        hallOfFame.setShowAuthId(1);
        PaginationParam paginationParam = new PaginationParam();
        List<HallOfFame> hallOfFameList = Arrays.asList(hallOfFame);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(hallOfFameRepository.findHallOfFameAfter(paginationParam)).thenReturn(hallOfFameList);
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(userHallOfFameHitCntRelationShipRepository.findByUserAndHallOfFame(user, hallOfFame)).thenReturn(Optional.empty());

        // Act
        CursorPagination<HallOfFameResponse> response = hallOfFameService.showHallOfFame(paginationParam);

        // Assert
        assertNotNull(response);
        assertFalse(response.getMeta().isHasMore());
        assertEquals(1, response.getData().size());
        verify(hallOfFameRepository).findHallOfFameAfter(paginationParam);
    }

    @Test
    void testRegisterHallOfFameSuccess() {
        // Arrange
        HallOfFameRegisterRequest request = new HallOfFameRegisterRequest();
        request.setShowAuthId(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(hallOfFameRepository.findByProjectId(1L)).thenReturn(Optional.empty());
        when(hallOfFameRepository.save(any(HallOfFame.class))).thenReturn(hallOfFame);

        // Act
        HallOfFamePostApiResponse response = hallOfFameService.registerHallOfFame(request);

        // Assert
        assertNotNull(response);
        verify(hallOfFameRepository).save(any(HallOfFame.class));
    }

    @Test
    void testRegisterHallOfFameDuplicate() {
        // Arrange
        HallOfFameRegisterRequest request = new HallOfFameRegisterRequest();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(hallOfFameRepository.findByProjectId(1L)).thenReturn(Optional.of(hallOfFame));

        // Act & Assert
        assertThrows(DuplicatedHallOfFameException.class, () -> hallOfFameService.registerHallOfFame(request));
    }

    @Test
    void testDeleteHallOfFameSuccess() throws IllegalAccessException {
        // Arrange
        HallOfFameDeleteRequest request = new HallOfFameDeleteRequest();
        request.setHallOfFameId("1");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(hallOfFameRepository.findById(1L)).thenReturn(Optional.of(hallOfFame));

        // Act
        HallOfFamePostApiResponse response = hallOfFameService.deleteHallOfFame(request);

        // Assert
        assertNotNull(response);
        verify(hallOfFameRepository).delete(hallOfFame);
    }

    @Test
    void testDeleteHallOfFameNotFound() {
        // Arrange
        HallOfFameDeleteRequest request = new HallOfFameDeleteRequest();
        request.setHallOfFameId("1");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(hallOfFameRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalAccessException.class, () -> hallOfFameService.deleteHallOfFame(request));
    }

    @Test
    void testHitGoodToHallOfFameFirstTime() throws IllegalAccessException {
        // Arrange
        HitGoodToProjectRequest request = new HitGoodToProjectRequest();
        request.setHallOfFameId("1");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(hallOfFameRepository.findById(1L)).thenReturn(Optional.of(hallOfFame));
        when(userHallOfFameHitCntRelationShipRepository.findByUserAndHallOfFame(user, hallOfFame)).thenReturn(Optional.empty());

        // Act
        HallOfFamePostApiResponse response = hallOfFameService.hitGoodToHallOfFame(request);

        // Assert
        assertNotNull(response);
        assertEquals(1, hallOfFame.getHitCnt());
        verify(userHallOfFameHitCntRelationShipRepository).save(any(UserHallOfFameHitCntRelationship.class));
    }

    @Test
    void testHitGoodToHallOfFameRemoveGood() throws IllegalAccessException {
        // Arrange
        HitGoodToProjectRequest request = new HitGoodToProjectRequest();
        request.setHallOfFameId("1");
        hallOfFame.setHitCnt(1);

        UserHallOfFameHitCntRelationship existingRelationship = new UserHallOfFameHitCntRelationship();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(hallOfFameRepository.findById(1L)).thenReturn(Optional.of(hallOfFame));
        when(userHallOfFameHitCntRelationShipRepository.findByUserAndHallOfFame(user, hallOfFame)).thenReturn(Optional.of(existingRelationship));

        // Act
        HallOfFamePostApiResponse response = hallOfFameService.hitGoodToHallOfFame(request);

        // Assert
        assertNotNull(response);
        assertEquals(0, hallOfFame.getHitCnt());
        verify(userHallOfFameHitCntRelationShipRepository).delete(existingRelationship);
    }

    @Test
    void testHitGoodToHallOfFameNotFound() {
        // Arrange
        HitGoodToProjectRequest request = new HitGoodToProjectRequest();
        request.setHallOfFameId("1");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(hallOfFameRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalAccessException.class, () -> hallOfFameService.hitGoodToHallOfFame(request));
    }
}

