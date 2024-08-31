package com.greaticker.demo.dto.response.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greaticker.demo.dto.response.diary.DiaryResponse;
import com.greaticker.demo.model.user.User;
import com.greaticker.demo.utils.StringConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {

    private String id;
    private String nickname;

    public static UserResponse fromEntity(User entity) {
        return new UserResponse(StringConverter.longToStringConvert(entity.getId()), entity.getNickname());
    }
}
