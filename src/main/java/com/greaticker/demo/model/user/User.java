package com.greaticker.demo.model.user;

import com.greaticker.demo.model.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "auth_id", nullable = false)
    private String authId;

    @Column(name = "sticker_inventory", nullable = false, columnDefinition = "varchar(255) default '[]'")
    private String stickerInventory;

    @Column(name = "hit_favorite_list", nullable = false, columnDefinition = "varchar(255) default '[]'")
    private String hitFavoriteList;

    @Column(name = "last_Get", nullable = true)
    private LocalDateTime lastGet;

    @Column(name = "now_project_id", nullable = true)
    private Long nowProjectId;

}
