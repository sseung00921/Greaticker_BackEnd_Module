package com.greaticker.demo.repository.user;

import com.greaticker.demo.model.hallOfFame.HallOfFame;
import com.greaticker.demo.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByNickname(String Nickname);

    Optional<User> findByAuthId(String googleId);
}
