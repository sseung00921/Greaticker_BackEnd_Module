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
public class HitFavoriteStickerResponse {
    private String stickerId;

    public static HitFavoriteStickerResponse fromEntity(Sticker entity) {
        return new HitFavoriteStickerResponse(StringConverter.longToStringConvert(entity.getId()));
    }
}
