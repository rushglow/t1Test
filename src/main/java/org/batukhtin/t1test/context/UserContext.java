package org.batukhtin.t1test.context;

import lombok.RequiredArgsConstructor;
import org.batukhtin.t1test.model.UserEntity;
import org.batukhtin.t1test.repository.UserRepository;
import org.batukhtin.t1test.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserContext {

    private final UserRepository userRepository;

    public Optional<UserEntity> getCurrentUser() {
        var currentUserIdentifier =  SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        var entity = userRepository.findUserByUsername(currentUserIdentifier);
        return entity;
    }

}
