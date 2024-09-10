package com.greaticker.demo.repository.sticker;

import com.greaticker.demo.model.project.Project;
import com.greaticker.demo.model.sticker.Sticker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StickerRepository extends JpaRepository<Sticker, Long> {
    List<Sticker> findAllByOrderByHitCntDesc();
}
