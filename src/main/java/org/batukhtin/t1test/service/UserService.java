package org.batukhtin.t1test.service;

import org.apache.coyote.BadRequestException;
import org.batukhtin.t1test.model.UserEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;


public interface UserService {
    UserEntity createUser(UserEntity user);

    Optional<UserEntity> findUserByUsername(String username);
}
