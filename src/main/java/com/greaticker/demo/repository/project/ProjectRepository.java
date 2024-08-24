package com.greaticker.demo.repository.project;

import com.greaticker.demo.model.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findByUserId(Long userId);
}
