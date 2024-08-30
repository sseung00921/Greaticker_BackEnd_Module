package com.greaticker.demo.config.securityConfig;

import com.greaticker.demo.model.user.User;
import com.greaticker.demo.repository.user.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String authId) throws UsernameNotFoundException {
        User user = userRepository.findByAuthId(authId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + authId));

        return new org.springframework.security.core.userdetails.User(
                user.getAuthId(),
                "",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))

        );
    }
}

