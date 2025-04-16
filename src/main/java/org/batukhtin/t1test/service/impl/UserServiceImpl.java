package org.batukhtin.t1test.service.impl;

import lombok.RequiredArgsConstructor;
import org.batukhtin.t1test.dto.RegDto;
import org.batukhtin.t1test.dto.UserDto;
import org.batukhtin.t1test.exception.ResourceNotFoundException;
import org.batukhtin.t1test.exception.UserAlreadyExists;
import org.batukhtin.t1test.mapper.UserMapper;
import org.batukhtin.t1test.model.UserEntity;
import org.batukhtin.t1test.repository.UserRepository;
import org.batukhtin.t1test.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    @org.batukhtin.t1starter.aspect.annotation.HandlingResult
    public UserDto createUser(RegDto regDto) throws RuntimeException  {
        userRepository.findUserByUsername(regDto.getUsername()).ifPresent(user -> {
                    throw new UserAlreadyExists("User with this username already exists");
                });
        return userMapper.toUserDto(userRepository.save(userMapper.toUserEntity(regDto)));
    }

    @Override
    @Transactional
    public UserEntity findUserByUsername(String username) {
        UserEntity userEntity = userRepository.findUserByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userEntity;
    }
}
