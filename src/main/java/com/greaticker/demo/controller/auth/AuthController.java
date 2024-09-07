package com.greaticker.demo.controller.auth;

import com.greaticker.demo.dto.response.auth.LoginResponse;
import com.greaticker.demo.dto.response.auth.UserResponse;
import com.greaticker.demo.dto.response.common.ApiResponse;
import com.greaticker.demo.service.auth.AuthService;
import com.greaticker.demo.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static com.greaticker.demo.constants.PlatForm.ANDROID;
import static com.greaticker.demo.constants.PlatForm.iOS;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @GetMapping("/get-me")
    public ResponseEntity<ApiResponse<UserResponse>> getMe(@RequestHeader("Authorization") String authHeader) {
        UserResponse nowUserResponse = userService.getCurrentUserResponse();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, null, nowUserResponse));
    }

    @PostMapping("/google")
    public ResponseEntity<ApiResponse<LoginResponse>> authenticateGoogleUser(@RequestHeader("Authorization") String authHeader,
                                                                             @RequestHeader("X-Platform") String platForm) throws GeneralSecurityException, IOException {

        System.out.println("aaaaaaaa");

        LoginResponse loginResponse = authService.authenticateGoogleUser(authHeader, platForm);
        if (loginResponse == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "Payload is null. Token might be Invalid.", null));
        }
        System.out.println("bbbbbbbb");
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, null, loginResponse));
    }



    @PostMapping("/delete-account")
    public ResponseEntity<ApiResponse<UserResponse>> deleteAccount()  {
        UserResponse deletedUserResponse = userService.deleteAccount();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, null, deletedUserResponse));
    }




}
