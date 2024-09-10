package com.greaticker.demo.service.profile;

import com.greaticker.demo.dto.request.profile.ChangeNicknameRequest;
import com.greaticker.demo.dto.response.profile.ChangeNicknameResultResponse;
import com.greaticker.demo.dto.response.profile.ProfileResponse;
import com.greaticker.demo.exception.customException.*;
import com.greaticker.demo.model.user.User;
import com.greaticker.demo.repository.user.UserRepository;
import com.greaticker.demo.service.user.UserService;
import com.greaticker.demo.utils.NamingRule;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.greaticker.demo.constants.StringLimit.*;
import static com.greaticker.demo.exception.errorCode.ErrorCode.*;

@Service
@Transactional
@AllArgsConstructor
public class ProfileService {

    final UserService userService;
    final UserRepository userRepository;

    public ChangeNicknameResultResponse changeNickname(ChangeNicknameRequest changeNicknameRequest) {
        User user = userService.getCurrentUser();
        String requestedNewNickname = changeNicknameRequest.getNewNickname();
        Optional<User> checkIfTheNicknameExist = userRepository.findByNickname(requestedNewNickname);
        if (checkIfTheNicknameExist.isPresent()) {
            throw new DuplicatedNicknameException(DUPLICATED_NICKNAME);
        }
        checkNamingRule(requestedNewNickname);

        user.setNickname(requestedNewNickname);
        return ChangeNicknameResultResponse.fromEntity(user);
    }

    private void checkNamingRule(String requestedNickname) {
        NamingRule.validateNoSpecialCharactersContainingEvenSpace(requestedNickname);
        NamingRule.validateNoStartWithGuest(requestedNickname);
        if (NamingRule.calculateLength(requestedNickname) > NICKNAME_LENGTH_LIMIT) {
            throw new TooLongNicknameException(TOO_LONG_NICKNAME);
        } else if (NamingRule.calculateLength(requestedNickname) < NICKNAME_LENGTH_UNDER_LIMIT) {
            throw new TooShortNicknameException(TOO_SHORT_NICKNAME);
        }
    }

    public ProfileResponse getProfile() {
        User user = userService.getCurrentUser();
        return ProfileResponse.fromEntity(user);
    }
}
