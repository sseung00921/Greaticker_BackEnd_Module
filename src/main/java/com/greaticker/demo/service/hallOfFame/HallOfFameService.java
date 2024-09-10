package com.greaticker.demo.service.hallOfFame;

import com.greaticker.demo.dto.request.common.PaginationParam;
import com.greaticker.demo.dto.request.hallOfFame.HallOfFameDeleteRequest;
import com.greaticker.demo.dto.request.hallOfFame.HallOfFameRegisterRequest;
import com.greaticker.demo.dto.request.hallOfFame.HitGoodToProjectRequest;
import com.greaticker.demo.dto.response.common.CursorPagination;
import com.greaticker.demo.dto.response.common.CursorPaginationMeta;
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
import com.greaticker.demo.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.greaticker.demo.constants.pagination.PaginationConstant.DEFAULT_FETCH_COUNT;
import static com.greaticker.demo.exception.errorCode.ErrorCode.DUPLICATED_HALL_OF_FAME;

@Service
@Transactional
@AllArgsConstructor
public class HallOfFameService {

    private final UserService userService;
    private final ProjectRepository projectRepository;
    private final HallOfFameRepository hallOfFameRepository;
    private final UserHallOfFameHitCntRelationShipRepository userHallOfFameHitCntRelationShipRepository;

    public CursorPagination<HallOfFameResponse> showHallOfFame(PaginationParam paginationParam) throws IllegalAccessException {
        User user = userService.getCurrentUser();
        List<HallOfFame> fetchedData = hallOfFameRepository.findHallOfFameAfter(paginationParam);

        boolean hasMore = fetchedData.size() > DEFAULT_FETCH_COUNT;
        if (hasMore) {
            fetchedData = fetchedData.subList(0, DEFAULT_FETCH_COUNT);
        }

        List<HallOfFameResponse> HallOfFameResponseList = new ArrayList<>();
        for (HallOfFame e : fetchedData) {
            String projectName = getProjectName(e);
            boolean isWrittenByMe = e.getUser() == user;
            boolean isHitGoodByMe = userHallOfFameHitCntRelationShipRepository.findByUserAndHallOfFame(user, e).isPresent();
            HallOfFameResponseList.add(HallOfFameResponse.fromEntity(e, projectName, isWrittenByMe, isHitGoodByMe));
        }
        return new CursorPagination<>(new CursorPaginationMeta(DEFAULT_FETCH_COUNT, hasMore), HallOfFameResponseList);
    }

    private String getProjectName(HallOfFame e) throws IllegalAccessException {
        Optional<Project> fetchedData = projectRepository.findById(e.getProjectId());
        if (fetchedData.isEmpty()) {
            throw new IllegalAccessException("project can not be null since hall of fame was already made with that project");
        }
        return fetchedData.get().getName();
    }

    public HallOfFamePostApiResponse registerHallOfFame(HallOfFameRegisterRequest hallOfFameRegisterRequest) {
        User user = userService.getCurrentUser();
        Long projectId = Long.valueOf(user.getNowProjectId());
        Optional<HallOfFame> checkProjectExist = hallOfFameRepository.findByProjectId(projectId);
        if (checkProjectExist.isPresent()) {
            throw new DuplicatedHallOfFameException(DUPLICATED_HALL_OF_FAME);
        }
        HallOfFame newlyRegisteredHallOfFame =
                hallOfFameRepository.save(new HallOfFame(null, user, projectId, 0, hallOfFameRegisterRequest.isShowAuthEmail()?  1 : 0));

        return HallOfFamePostApiResponse.fromEntity(newlyRegisteredHallOfFame);

    }

    public HallOfFamePostApiResponse deleteHallOfFame(HallOfFameDeleteRequest hallOfFameDeleteRequest) throws IllegalAccessException {
        User user = userService.getCurrentUser();
        Long hallOfFameId = Long.valueOf(hallOfFameDeleteRequest.getHallOfFameId());
        Optional<HallOfFame> fetchedData = hallOfFameRepository.findById(hallOfFameId);
        if (fetchedData.isEmpty()) {
            throw new IllegalAccessException("hall of fame can not be null since delete icon was clicked");
        }
        HallOfFame toBeDeletedHallOfFame = fetchedData.get();
        HallOfFamePostApiResponse result = HallOfFamePostApiResponse.fromEntity(toBeDeletedHallOfFame);
        hallOfFameRepository.delete(toBeDeletedHallOfFame);

        return result;
    }

    public HallOfFamePostApiResponse hitGoodToHallOfFame(HitGoodToProjectRequest hitGoodToProjectRequest) throws IllegalAccessException {
        User user = userService.getCurrentUser();
        Long hallOfFameId = Long.valueOf(hitGoodToProjectRequest.getHallOfFameId());
        Optional<HallOfFame> fetchedData = hallOfFameRepository.findById(hallOfFameId);
        if (fetchedData.isEmpty()) {
            throw new IllegalAccessException("hall of fame can not be null since hit-good icon was clicked");
        }
        HallOfFame hitHallOfFame = fetchedData.get();
        Optional<UserHallOfFameHitCntRelationship> checkIfAlreadyHitGood = userHallOfFameHitCntRelationShipRepository.findByUserAndHallOfFame(user, hitHallOfFame);
        if (checkIfAlreadyHitGood.isEmpty()) {
            hitHallOfFame.plusHitCnt();
            userHallOfFameHitCntRelationShipRepository.save(new UserHallOfFameHitCntRelationship(null, user, hitHallOfFame));
        } else {
            UserHallOfFameHitCntRelationship alreadyHitInfo = checkIfAlreadyHitGood.get();
            hitHallOfFame.minusHit();
            userHallOfFameHitCntRelationShipRepository.delete(alreadyHitInfo);
        }

        return HallOfFamePostApiResponse.fromEntity(hitHallOfFame);
    }


}
