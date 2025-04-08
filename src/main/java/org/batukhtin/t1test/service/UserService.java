package org.batukhtin.t1test.service;

import org.batukhtin.t1test.dto.RegDto;
import org.batukhtin.t1test.dto.UserDto;
import org.batukhtin.t1test.model.UserEntity;


public interface UserService {
    UserDto createUser(RegDto regDto);

    UserEntity findUserByUsername(String username);
}
