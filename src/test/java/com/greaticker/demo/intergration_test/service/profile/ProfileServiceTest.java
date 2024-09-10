package com.greaticker.demo.intergration_test.service.profile;

import com.greaticker.demo.dto.request.profile.ChangeNicknameRequest;
import com.greaticker.demo.dto.response.profile.ChangeNicknameResultResponse;
import com.greaticker.demo.dto.response.profile.ProfileResponse;
import com.greaticker.demo.exception.customException.DuplicatedNicknameException;
import com.greaticker.demo.exception.customException.TooLongNicknameException;
import com.greaticker.demo.exception.customException.TooShortNicknameException;
import com.greaticker.demo.model.user.User;
import com.greaticker.demo.repository.user.UserRepository;
import com.greaticker.demo.service.profile.ProfileService;
import com.greaticker.demo.service.user.UserService;
import com.greaticker.demo.utils.NamingRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static com.greaticker.demo.constants.StringLimit.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class ProfileServiceTest {

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ProfileService profileService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setNickname("OldNickname");
    }

    @Test
    void testChangeNicknameSuccess() {
        // Arrange
        ChangeNicknameRequest request = new ChangeNicknameRequest();
        request.setNewNickname("NewNickname");

        when(userService.getCurrentUser()).thenReturn(user);
        when(userRepository.findByNickname("NewNickname")).thenReturn(Optional.empty());

        // Act
        ChangeNicknameResultResponse response = profileService.changeNickname(request);

        // Assert
        assertNotNull(response);
        assertEquals("NewNickname", response.getNewUserNickname());
        verify(userService).getCurrentUser();
        verify(userRepository).findByNickname("NewNickname");
    }

    @Test
    void testChangeNicknameDuplicate() {
        // Arrange
        ChangeNicknameRequest request = new ChangeNicknameRequest();
        request.setNewNickname("ExistingNickname");

        User existingUser = new User();
        existingUser.setNickname("ExistingNickname");

        when(userService.getCurrentUser()).thenReturn(user);
        when(userRepository.findByNickname("ExistingNickname")).thenReturn(Optional.of(existingUser));

        // Act & Assert
        assertThrows(DuplicatedNicknameException.class, () -> profileService.changeNickname(request));
    }

    @Test
    void testChangeNicknameTooLong() {
        // Arrange
        String longNickname = "A".repeat((NICKNAME_LENGTH_LIMIT/3) + 1);
        ChangeNicknameRequest request = new ChangeNicknameRequest();
        request.setNewNickname(longNickname);

        when(userService.getCurrentUser()).thenReturn(user);

        // Act & Assert
        assertThrows(TooLongNicknameException.class, () -> profileService.changeNickname(request));
    }

    @Test
    void testChangeNicknameTooShort() {
        // Arrange
        String shortNickname = "A".repeat((NICKNAME_LENGTH_UNDER_LIMIT/3) - 1);
        ChangeNicknameRequest request = new ChangeNicknameRequest();
        request.setNewNickname(shortNickname);

        when(userService.getCurrentUser()).thenReturn(user);

        // Act & Assert
        assertThrows(TooShortNicknameException.class, () -> profileService.changeNickname(request));
    }

    @Test
    void testGetProfile() {
        // Arrange
        when(userService.getCurrentUser()).thenReturn(user);

        // Act
        ProfileResponse response = profileService.getProfile();

        // Assert
        assertNotNull(response);
        assertEquals("OldNickname", response.getUserNickname());
        verify(userService).getCurrentUser();
    }
}
