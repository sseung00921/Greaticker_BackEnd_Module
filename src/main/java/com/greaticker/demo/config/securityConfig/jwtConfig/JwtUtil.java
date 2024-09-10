package com.greaticker.demo.config.securityConfig.jwtConfig;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.time.Instant;
import java.util.Date;

import static com.greaticker.demo.constants.auth.Auth.*;

@Component
public class JwtUtil {

    @Value("${my.app.secret-key}") private String secretKey;

    public String extractUsername(String token) throws IOException, ParseException, BadJOSEException, JOSEException {
        // JWKSet을 수동으로 로드
        JWKSet jwkSet = loadJWKSet(new URL(COGNITO_JWK_URL));
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(jwkSet);

        // Processor configuration
        ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
        JWSVerificationKeySelector<SecurityContext> keySelector = new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, jwkSource);
        jwtProcessor.setJWSKeySelector(keySelector);
        // Parse and verify the token
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWTClaimsSet claimsSet = jwtProcessor.process(signedJWT, null);

        // Extract google_id (sub) from claims
        String auth_id = claimsSet.getSubject();

        return auth_id;
    }

    public Boolean validateToken(String token, UserDetails userDetails) throws Exception {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && checkClaimsFromToken(token));
    }

    public Boolean checkClaimsFromToken(String idToken) throws Exception {
        // Cognito 공개 키 가져오기
        JWKSet jwkSet = loadJWKSet(new URL(COGNITO_JWK_URL));
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(jwkSet);

        // JWT Processor 설정
        ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
        JWSVerificationKeySelector<SecurityContext> keySelector = new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, jwkSource);
        jwtProcessor.setJWSKeySelector(keySelector);

        // JWT 토큰 서명 검증 및 클레임 세트 추출
        SignedJWT signedJWT = SignedJWT.parse(idToken);
        JWTClaimsSet claimsSet = jwtProcessor.process(signedJWT, null);


        if (!COGNITO_ISSUER.equals(claimsSet.getIssuer())) {
            throw new Exception("Invalid issuer");
        }

        // 대상자(audience) 확인
        if (!claimsSet.getAudience().contains(COGNITO_AUDIENCE)) {
            throw new Exception("Invalid audience");
        }

        // 만료 시간(exp) 확인
        Date expirationTime = claimsSet.getExpirationTime();
        if (expirationTime.before(Date.from(Instant.now()))) {
            throw new Exception("Token is expired");
        }

        return true;
    }

    private JWKSet loadJWKSet(URL url) throws IOException, ParseException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        try (InputStream inputStream = con.getInputStream()) {
            return JWKSet.load(inputStream);
        }
    }
}

