package com.greaticker.demo.dto.response.hallOfFame;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.greaticker.demo.model.hallOfFame.HallOfFame;
import com.greaticker.demo.model.user.User;
import com.greaticker.demo.utils.StringConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HallOfFameResponse {

    private String id;
    // 유저 닉네임
    private String userNickName;
    // 유저 auth ID
    private String userAuthId;
    // 달성한 목표
    private String accomplishedGoal;
    // 좋아요 횟수
    private Integer likeCount;
    // 내가 작성한 명전 카드인지 여부
    private Boolean isWrittenByMe;
    private Boolean isHitGoodByMe;

    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;

    public static HallOfFameResponse fromEntity(HallOfFame entity, String projectName, boolean isWrittenByMe, boolean isHitGoodByMe) {
        User user = entity.getUser();
        String authId = entity.getShowAuthId() == 1 ? user.getAuthId() : null;
        return new HallOfFameResponse(StringConverter.longToStringConvert(entity.getId()), user.getNickname(),
                authId, projectName, entity.getHitCnt(), isWrittenByMe, isHitGoodByMe, entity.getCreatedDateTime(), entity.getUpdatedDateTime());
    }
}
