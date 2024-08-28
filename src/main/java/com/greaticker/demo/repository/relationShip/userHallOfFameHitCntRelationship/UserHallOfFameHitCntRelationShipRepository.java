package com.greaticker.demo.repository.relationShip.userHallOfFameHitCntRelationship;

import com.greaticker.demo.model.hallOfFame.HallOfFame;
import com.greaticker.demo.model.relationShip.userHallOfFameHitCntRelationShip.UserHallOfFameHitCntRelationship;
import com.greaticker.demo.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserHallOfFameHitCntRelationShipRepository extends JpaRepository<UserHallOfFameHitCntRelationship, Long> {


    Optional<UserHallOfFameHitCntRelationship> findByUserAndHallOfFame(User user, HallOfFame hitHallOfFame);
}
