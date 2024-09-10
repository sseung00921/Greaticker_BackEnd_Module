package com.greaticker.demo.repository.hallOfFame;

import com.greaticker.demo.model.hallOfFame.HallOfFame;
import com.greaticker.demo.model.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HallOfFameRepository extends JpaRepository<HallOfFame, Long>, HallOfFameRepositoryCustom {

    Optional<HallOfFame> findByProjectId(Long projectId);
}
