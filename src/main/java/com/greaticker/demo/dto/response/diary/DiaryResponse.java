package com.greaticker.demo.dto.response.diary;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greaticker.demo.constants.enums.history.HistoryKind;
import com.greaticker.demo.model.history.History;
import com.greaticker.demo.model.user.User;
import com.greaticker.demo.utils.StringConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DiaryResponse {

    private List<String> stickerInventory;
    private Set<String> hitFavoriteList;

    public static DiaryResponse fromEntity(User entity) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        List<String> stickerInventoryList = om.readValue(entity.getStickerInventory(), new TypeReference<List<String>>() {
        });
        Set<String> hitFavoriteListSet = om.readValue(entity.getHitFavoriteList(), new TypeReference<Set<String>>() {
        });
        return new DiaryResponse(stickerInventoryList, hitFavoriteListSet);
    }
}
