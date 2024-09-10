package com.greaticker.demo.dto.response.hallOfFame;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.greaticker.demo.model.hallOfFame.HallOfFame;
import com.greaticker.demo.model.user.User;
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
public class HallOfFamePostApiResponse {

    private String hallOfFameId;

    public static HallOfFamePostApiResponse fromEntity(HallOfFame entity) {
        return new HallOfFamePostApiResponse(StringConverter.longToStringConvert(entity.getId()));
    }
}
