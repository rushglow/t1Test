package org.batukhtin.t1test.service.impl;

import org.batukhtin.t1test.dto.RegDto;
import org.batukhtin.t1test.dto.UserDto;
import org.batukhtin.t1test.exception.ResourceNotFoundException;
import org.batukhtin.t1test.exception.UserAlreadyExists;
import org.batukhtin.t1test.mapper.UserMapper;
import org.batukhtin.t1test.model.UserEntity;
import org.batukhtin.t1test.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private RegDto regDto;
    private UserDto userDto;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        regDto = new RegDto();
        regDto.setUsername("test");
        regDto.setMail("test@test.com");
        regDto.setPassword("test");

        userDto = new UserDto();

        userEntity = new UserEntity();
    }

    @Test
    @DisplayName("Создание пользователя")
    void createUser() {

        when(userRepository.findUserByUsername("test")).thenReturn(Optional.empty());
        when(userMapper.toUserEntity(regDto)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userMapper.toUserDto(userEntity)).thenReturn(userDto);

        UserDto result = userService.createUser(regDto);

        assertNotNull(result);
        verify(userRepository).findUserByUsername("test");
        verify(userRepository).save(userEntity);
    }

    @Test
    @DisplayName("Попытка создания уже существующего ползователя")
    void createUser_whenUserAlreadyExists() {
        when(userRepository.findUserByUsername("test"))
                .thenReturn(Optional.of(new UserEntity()));

        assertThrows(UserAlreadyExists.class, () -> userService.createUser(regDto));
        verify(userRepository).findUserByUsername("test");
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Поиск пользователя по username")
    void findUserByUsername() {
        String username = "test";
        UserEntity entity = new UserEntity();
        when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(entity));

        UserEntity result = userService.findUserByUsername(username);

        assertNotNull(result);
        assertEquals(entity, result);
        verify(userRepository).findUserByUsername(username);
    }

    @Test
    @DisplayName("Поиск пользователя несуществующего пользователя")
    void findUserByUsername_userNotFoundException() {
        when(userRepository.findUserByUsername("test")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.findUserByUsername("test"));
        verify(userRepository).findUserByUsername("test");
    }
}