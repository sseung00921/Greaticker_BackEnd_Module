package com.greaticker.demo.model.history;

import com.greaticker.demo.constants.enums.history.HistoryKind;
import com.greaticker.demo.model.common.BaseEntity;
import com.greaticker.demo.model.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class History extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kind", nullable = false)
    @Enumerated(EnumType.STRING)
    private HistoryKind kind;

    @Column(name = "project_name", nullable = false)
    private String project_name;

    @Column(name = "day_in_a_row")
    private Integer day_in_a_row;

    @Column(name = "sticker_id")
    private Long sticker_id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
