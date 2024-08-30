package com.greaticker.demo.controller.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.greaticker.demo.dto.response.auth.LoginResponse;
import com.greaticker.demo.dto.response.auth.UserResponse;
import com.greaticker.demo.dto.response.common.ApiResponse;
import com.greaticker.demo.model.user.User;
import com.greaticker.demo.repository.user.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@AllArgsConstructor
public class AuthController {

    private UserRepository userRepository;

    @Value("my.app.secret-key") private String secretKey;

    @GetMapping("/get-me")
    public ResponseEntity<ApiResponse<UserResponse>> getMe(@RequestHeader("Authorization") String authHeader) throws GeneralSecurityException, IOException {
        String jwtToken = extractTokenFromAuthHeader(authHeader);
        try {
            SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
            // JWT 토큰을 파싱하고 유저 정보 추출
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwtToken)
                    .getBody();

            // 유저 정보 추출 (예: 사용자 ID 또는 이메일)
            String userId = claims.getSubject(); // 토큰의 서브젝트에서 유저 ID 추출

            Optional<User> checkIfUserExist = userRepository.findById(Long.valueOf(userId));
            if (checkIfUserExist.isPresent()) {
                User user = checkIfUserExist.get();
                // LoginResponse 객체 생성 (필요한 유저 정보 포함)
                UserResponse userResponse = new UserResponse();
                userResponse.setId(userId);
                userResponse.setNickname(user.getNickname());

                // ApiResponse를 성공 상태로 반환
                return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, null, userResponse));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Get Me was failed. It might be Invalid or expired JWT token.", null));
            }
        } catch (SignatureException e) {
            // 토큰 검증 실패 시 처리
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "Get Me was failed. It might be Invalid or expired JWT token.", null));
        }
    }

    @PostMapping("/google")
    public ResponseEntity<ApiResponse<LoginResponse>> authenticateGoogleUser(@RequestHeader("Authorization") String authHeader,
                                                                             @RequestHeader("X-Platform") String platForm) throws GeneralSecurityException, IOException {
        String googleIdToken = extractTokenFromAuthHeader(authHeader);

        GoogleIdToken.Payload payload = verifyGoogleToken(googleIdToken, platForm);

        if (payload == null) {
            throw new RuntimeException("Payload is null. Token might be Invalid.");
        }

        String googleId = payload.getSubject();

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
            user.setNickname("Guest" + registeredUserCnt);
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

        Key key = new SecretKeySpec(Base64.getDecoder().decode(secretKey), SignatureAlgorithm.HS256.getJcaName());

        return Jwts.builder()
                .setSubject(user.getAuthId()) // 토큰의 주체 설정 (일반적으로 사용자 ID)
                .claim("nickname", user.getNickname()) // 추가 클레임 (필요한 정보를 추가)
                .setIssuedAt(now) // 토큰 발행 시간
                .setExpiration(expiryDate) // 만료 시간
                .signWith(key, SignatureAlgorithm.HS512) // 서명 알고리즘 및 비밀 키
                .compact();
    }
}
