package com.greaticker.demo.config.securityConfig;

import com.greaticker.demo.constants.RedisKeys;
import com.greaticker.demo.model.user.User;
import com.greaticker.demo.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String authId) throws UsernameNotFoundException {
        String redisKey = RedisKeys.USER_DETAIL + authId;

        // Redis에서 사용자 정보 우선 조회 캐시 히트시 DB를 별도로 타지 않는다. t2.micro 배포를 위한 타협
//        User user = redisTemplate.opsForValue().get(redisKey);
//        if (user != null) {
//            return getUserDetailsFromUser(user);
//        }

        User user = userRepository.findByAuthId(authId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + authId));

        // Redis에 사용자 정보 저장
        //redisTemplate.opsForValue().set(redisKey, user, Duration.ofDays(1));

        return getUserDetailsFromUser(user);
    }

    private static org.springframework.security.core.userdetails.User getUserDetailsFromUser(User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getAuthId(),
                "",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}

