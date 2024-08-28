package com.greaticker.demo.service.hallOfFame;

import com.greaticker.demo.dto.request.hallOfFame.HallOfFameDeleteRequest;
import com.greaticker.demo.dto.request.hallOfFame.HallOfFameRegisterRequest;
import com.greaticker.demo.dto.request.hallOfFame.HitGoodToProjectRequest;
import com.greaticker.demo.dto.response.hallOfFame.HallOfFamePostApiResponse;
import com.greaticker.demo.exception.customException.DuplicatedHallOfFameException;
import com.greaticker.demo.model.hallOfFame.HallOfFame;
import com.greaticker.demo.model.project.Project;
import com.greaticker.demo.model.relationShip.userHallOfFameHitCntRelationShip.UserHallOfFameHitCntRelationship;
import com.greaticker.demo.model.user.User;
import com.greaticker.demo.repository.hallOfFame.HallOfFameRepository;
import com.greaticker.demo.repository.project.ProjectRepository;
import com.greaticker.demo.repository.relationShip.userHallOfFameHitCntRelationship.UserHallOfFameHitCntRelationShipRepository;
import com.greaticker.demo.repository.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.greaticker.demo.exception.errorCode.ErrorCode.DUPLICATED_HALL_OF_FAME;

@Service
@Transactional
@AllArgsConstructor
public class HallOfFameService {

    final UserRepository userRepository;
    final ProjectRepository projectRepository;
    final HallOfFameRepository hallOfFameRepository;
    final UserHallOfFameHitCntRelationShipRepository userHallOfFameHitCntRelationShipRepository;

    public HallOfFamePostApiResponse registerHallOfFame(HallOfFameRegisterRequest hallOfFameRegisterRequest) {
        User user = userRepository.findById(1L).get();
        Long projectId = Long.valueOf(hallOfFameRegisterRequest.getProjectId());
        Optional<Project> checkProjectExist = projectRepository.findById(projectId);
        if (!checkProjectExist.isEmpty()) {
            throw new DuplicatedHallOfFameException(DUPLICATED_HALL_OF_FAME);
        }
        HallOfFame newlyRegisteredHallOfFame =
                hallOfFameRepository.save(new HallOfFame(null, user, projectId, 0, hallOfFameRegisterRequest.isShowAuthId()?  1 : 0));

        return HallOfFamePostApiResponse.fromEntity(newlyRegisteredHallOfFame);

    }

    public HallOfFamePostApiResponse deleteHallOfFame(HallOfFameDeleteRequest hallOfFameDeleteRequest) throws IllegalAccessException {
        User user = userRepository.findById(1L).get();
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
        User user = userRepository.findById(1L).get();
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
