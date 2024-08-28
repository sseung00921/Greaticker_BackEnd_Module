package com.greaticker.demo.dto.response.sticker;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.greaticker.demo.model.sticker.Sticker;
import com.greaticker.demo.utils.StringConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StickerRankingResponse {
    private String id;
    private Integer rank;
    private Integer hitCnt;

    public static StickerRankingResponse fromEntity(Sticker entity) {
        return new StickerRankingResponse(StringConverter.longToStringConvert(entity.getId()), null, entity.getHitCnt());
    }
}
