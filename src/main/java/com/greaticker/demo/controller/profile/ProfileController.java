package com.greaticker.demo.controller.profile;

import com.greaticker.demo.dto.request.common.PaginationParam;
import com.greaticker.demo.dto.request.profile.ChangeNicknameRequest;
import com.greaticker.demo.dto.response.common.ApiResponse;
import com.greaticker.demo.dto.response.common.CursorPagination;
import com.greaticker.demo.dto.response.hallOfFame.HallOfFameResponse;
import com.greaticker.demo.dto.response.profile.ChangeNicknameResultResponse;
import com.greaticker.demo.dto.response.profile.ProfileResponse;
import com.greaticker.demo.repository.user.UserRepository;
import com.greaticker.demo.service.profile.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("profile")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/")
    public ResponseEntity<ApiResponse<ProfileResponse>> getProfile() {
        ProfileResponse fetchedData = profileService.getProfile();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, null, fetchedData));
    }

    @PostMapping("/change-nickname")
    public ResponseEntity<ApiResponse<ChangeNicknameResultResponse>> changeNickname(@RequestBody ChangeNicknameRequest changeNicknameRequest) {
        ChangeNicknameResultResponse fetchedData = profileService.changeNickname(changeNicknameRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, null, fetchedData));
    }

}
