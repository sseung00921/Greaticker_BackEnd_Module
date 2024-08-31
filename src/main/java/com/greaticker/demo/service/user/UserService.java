package com.greaticker.demo.service.user;

import com.greaticker.demo.config.securityConfig.CustomUserDetailsService;
import com.greaticker.demo.dto.response.auth.UserResponse;
import com.greaticker.demo.model.user.User;
import com.greaticker.demo.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication.getPrincipal() instanceof UserDetails userDetails) {
                Optional<User> user = userRepository.findByAuthId(userDetails.getUsername());
                if (user.isEmpty()) {
                    throw new RuntimeException("pass jwt Token but user doesn't exist. this might not occur but just remain console log");
                }
                return user.get();
            }
        }
        throw new RuntimeException("pass jwt Token Security Context is Empty. this might not occur but just remain console log");
    }

    public UserResponse getCurrentUserResponse() {
        User user = getCurrentUser();
        return UserResponse.fromEntity(user);
    }

    public UserResponse deleteAccount() {
        User deletedUser = getCurrentUser();
        UserResponse deletedUserResponse = UserResponse.fromEntity(deletedUser);
        userRepository.delete(deletedUser);
        return deletedUserResponse;
    }
}
