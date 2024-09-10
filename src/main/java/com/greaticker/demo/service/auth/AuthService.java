package com.greaticker.demo.service.auth;

import com.greaticker.demo.config.securityConfig.CustomUserDetailsService;
import com.greaticker.demo.dto.response.auth.LoginResponse;
import com.greaticker.demo.model.user.User;
import com.greaticker.demo.repository.user.UserRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.util.Optional;

import static com.greaticker.demo.constants.auth.Auth.COGNITO_JWK_URL;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final CustomUserDetailsService customUserDetailsService;

    @Value("${my.app.secret-key}") private String secretKey;

    public LoginResponse authenticateGoogleUser(String authHeader, String platForm) throws GeneralSecurityException, IOException, ParseException, BadJOSEException, JOSEException {
        String idToken = extractTokenFromAuthHeader(authHeader);

        // JWKSet을 수동으로 로드
        JWKSet jwkSet = loadJWKSet(new URL(COGNITO_JWK_URL));
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(jwkSet);

        // Processor configuration
        ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
        JWSVerificationKeySelector<SecurityContext> keySelector = new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, jwkSource);
        jwtProcessor.setJWSKeySelector(keySelector);
        // Parse and verify the token
        SignedJWT signedJWT = SignedJWT.parse(idToken);
        JWTClaimsSet claimsSet = jwtProcessor.process(signedJWT, null);

        // Extract google_id (sub) from claims
        String auth_id = claimsSet.getSubject();
        String auth_email = (String) claimsSet.getClaim("email");

        // 데이터베이스에서 사용자를 찾습니다.
        Optional<User> existingUser = userRepository.findByAuthId(auth_id);
        User user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            // 사용자가 없다면 새로운 사용자 등록
            long registeredUserCnt = userRepository.count();
            user = new User();
            user.setAuthId(auth_id);
            user.setAuthEmail(auth_email);
            user.setNickname("User" + registeredUserCnt);
            user.setStickerInventory("[]");
            user.setHitFavoriteList("[]");
            userRepository.save(user);
        }

        authenticateUser(user.getAuthId());

        return new LoginResponse(idToken);
    }

    private JWKSet loadJWKSet(URL url) throws IOException, ParseException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        try (InputStream inputStream = con.getInputStream()) {
            return JWKSet.load(inputStream);
        }
    }

    private static String extractTokenFromAuthHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // "Bearer " 이후의 토큰 부분만 추출
        } else {
            throw new RuntimeException("auth Header format is Invalid. It doesn't start with Bearer");
        }
    }


    private void authenticateUser(String authId) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(authId);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
