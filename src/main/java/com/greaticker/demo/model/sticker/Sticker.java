package com.greaticker.demo.model.sticker;

import com.greaticker.demo.constants.enums.project.ProjectState;
import com.greaticker.demo.model.common.BaseEntity;
import com.greaticker.demo.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sticker extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "hit_cnt", nullable = false)
    private Integer hitCnt;

    public void plusHit() {
        this.hitCnt += 1;
    }

    public void minusHit() {
        if (hitCnt - 1 < 0) {
            throw new IllegalArgumentException("sticker hit cnt can not be negative");
        }
        this.hitCnt -= 1;
    }

}
