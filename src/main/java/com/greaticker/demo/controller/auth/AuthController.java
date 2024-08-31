package com.greaticker.demo.controller.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.greaticker.demo.config.securityConfig.CustomUserDetailsService;
import com.greaticker.demo.dto.response.auth.LoginResponse;
import com.greaticker.demo.dto.response.auth.UserResponse;
import com.greaticker.demo.dto.response.common.ApiResponse;
import com.greaticker.demo.model.user.User;
import com.greaticker.demo.repository.user.UserRepository;
import com.greaticker.demo.service.user.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static com.greaticker.demo.constants.PlatForm.iOS;
import static com.greaticker.demo.constants.auth.Auth.GOOGLE_OAUTH2_IOS_CLIENT_ID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final CustomUserDetailsService customUserDetailsService;

    @Value("${my.app.secret-key}") private String secretKey;

    @GetMapping("/get-me")
    public ResponseEntity<ApiResponse<UserResponse>> getMe(@RequestHeader("Authorization") String authHeader) throws GeneralSecurityException, IOException {
        User user = userService.getCurrentUser();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, null, UserResponse.fromEntity(user)));
    }

    @PostMapping("/google")
    public ResponseEntity<ApiResponse<LoginResponse>> authenticateGoogleUser(@RequestHeader("Authorization") String authHeader,
                                                                             @RequestHeader("X-Platform") String platForm) throws GeneralSecurityException, IOException {
        String googleIdToken = extractTokenFromAuthHeader(authHeader);
        GoogleIdToken.Payload payload = verifyGoogleToken(googleIdToken, platForm);

        if (payload == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "Payload is null. Token might be Invalid.", null));
        }

        String googleId = payload.getSubject();
        String googleEmail = payload.getEmail();

        // 데이터베이스에서 사용자를 찾습니다.
        Optional<User> existingUser = userRepository.findByAuthId(googleId);

        User user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            // 사용자가 없다면 새로운 사용자 등록
            long registeredUserCnt = userRepository.count();
            user = new User();
            user.setAuthId(googleId);
            user.setAuthEmail(googleEmail);
            user.setNickname("User" + registeredUserCnt);
            user.setStickerInventory("[]");
            user.setHitFavoriteList("[]");
            userRepository.save(user);
        }

        // JWT 생성 또는 세션 관리
        String jwtToken = generateJwtToken(user);
        LoginResponse loginResponse = new LoginResponse(jwtToken);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, null, loginResponse));
    }

    private static String extractTokenFromAuthHeader(String authHeader) {
        String googleIdToken = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            googleIdToken = authHeader.substring(7); // "Bearer " 이후의 토큰 부분만 추출
        } else {
            throw new RuntimeException("auth Header format is Invalid. It doesn't start with Bearer");
        }
        return googleIdToken;
    }

    private GoogleIdToken.Payload verifyGoogleToken(String idToken, String platForm) throws GeneralSecurityException, IOException {
        JsonFactory jsonFactory = new GsonFactory();
        String ClientId = platForm.equals(iOS) ? GOOGLE_OAUTH2_IOS_CLIENT_ID : "otherThingToAdd";

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), jsonFactory)
                .setAudience(Collections.singletonList(ClientId))
                .build();

        GoogleIdToken googleIdToken = verifier.verify(idToken);
        if (googleIdToken != null) {
            return googleIdToken.getPayload();
        } else {
            return null;
        }
    }

    private String generateJwtToken(User user) {
        long expirationTime = 86400000; // 1일(밀리초 단위)
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        Key key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey)); // Base64 디코딩된 비밀 키 사용

        String token = Jwts.builder()
                .setSubject(user.getAuthId())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();


        authenticateUser(user.getAuthId());

        return token;
    }

    private void authenticateUser(String authId) {
        // UserDetailsService를 통해 사용자 정보 로드
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(authId);

        // 사용자 인증 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails, // 사용자 정보 객체
                        null, // 자격 증명 (JWT 토큰은 이미 검증되었으므로 null로 설정)
                        userDetails.getAuthorities() // 사용자 권한 목록
                );


        // SecurityContext에 인증 객체 설정
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
