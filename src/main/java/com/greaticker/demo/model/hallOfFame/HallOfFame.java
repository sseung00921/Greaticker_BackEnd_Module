package com.greaticker.demo.model.hallOfFame;

import com.greaticker.demo.model.common.BaseEntity;
import com.greaticker.demo.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HallOfFame extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(name = "hit_cnt", nullable = false)
    private Integer hitCnt;

    @Column(name = "show_auth_id", nullable = false)
    private Integer showAuthEmail;


    public void plusHitCnt() {
        this.hitCnt += 1;
    }

    public void minusHit() {
        if (hitCnt - 1 < 0) {
            throw new IllegalArgumentException("hall of fame hit cnt can not be negative");
        }
        this.hitCnt -= 1;
    }
}
