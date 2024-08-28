package com.greaticker.demo.model.relationShip.userHallOfFameHitCntRelationShip;

import com.greaticker.demo.model.common.BaseEntity;
import com.greaticker.demo.model.hallOfFame.HallOfFame;
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
public class UserHallOfFameHitCntRelationship extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "hall_of_fame_id", nullable = false)
    private HallOfFame hallOfFame;


}
