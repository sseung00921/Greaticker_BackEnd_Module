package com.greaticker.demo.model.project;

import com.greaticker.demo.constants.enums.project.ProjectState;
import com.greaticker.demo.model.common.BaseEntity;
import com.greaticker.demo.model.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Project extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProjectState state;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime project_id;

    @Column(name = "day_in_a_row", nullable = false)
    private Integer day_in_a_row;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
