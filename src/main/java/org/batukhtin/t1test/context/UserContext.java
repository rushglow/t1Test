package org.batukhtin.t1test.context;

import lombok.RequiredArgsConstructor;
import org.batukhtin.t1test.model.UserEntity;
import org.batukhtin.t1test.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserContext {

    private final UserService userService;

    public Optional<UserEntity> getCurrentUser() {
        var currentUserIdentifier =  SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        var entity = userService.findUserByUsername(currentUserIdentifier);
        return entity;
    }

}
