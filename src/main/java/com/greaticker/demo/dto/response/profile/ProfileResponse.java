package com.greaticker.demo.dto.response.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.greaticker.demo.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileResponse {
    private String userNickname;


    public static ProfileResponse fromEntity(User entity) {
        return new ProfileResponse(entity.getNickname());
    }
}
