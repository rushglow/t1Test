package org.batukhtin.t1test.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.batukhtin.t1test.aspect.annotation.HandlingResult;
import org.batukhtin.t1test.aspect.annotation.LogException;
import org.batukhtin.t1test.exception.ResourceNotFoundException;
import org.batukhtin.t1test.model.UserEntity;
import org.batukhtin.t1test.repository.UserRepository;
import org.batukhtin.t1test.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    @HandlingResult
    public UserEntity createUser(UserEntity user)  {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public Optional<UserEntity> findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }
}
