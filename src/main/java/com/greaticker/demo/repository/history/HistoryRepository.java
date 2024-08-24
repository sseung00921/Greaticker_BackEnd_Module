package com.greaticker.demo.repository.history;

import com.greaticker.demo.model.history.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {

    List<History> findByUserId(Long userId);
}
