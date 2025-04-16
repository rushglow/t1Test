package org.batukhtin.t1test.mapper;

import lombok.RequiredArgsConstructor;
import org.batukhtin.t1test.dto.RegDto;
import org.batukhtin.t1test.dto.UserDto;
import org.batukhtin.t1test.model.UserEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public UserEntity toUserEntity(RegDto regDto) {
        return UserEntity.builder()
                .username(regDto.getUsername())
                .password(passwordEncoder.encode(regDto.getPassword()))
                .mail(regDto.getMail())
                .role("ROLE_USER")
                .build();
    }

    public UserDto toUserDto(UserEntity userEntity) {
        return UserDto.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .mail(userEntity.getMail())
                .role(userEntity.getRole())
                .build();
    }
}
