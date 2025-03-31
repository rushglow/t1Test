package org.batukhtin.t1test.controller;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.batukhtin.t1test.dto.RegDto;
import org.batukhtin.t1test.model.UserEntity;
import org.batukhtin.t1test.service.UserService;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegDto regDto) {
        if(userService.findUserByUsername(regDto.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        UserEntity user = new UserEntity().builder()
                .username(regDto.getUsername())
                .password(passwordEncoder.encode(regDto.getPassword()))
                .role("ROLE_USER")
                .build();
        userService.createUser(user);
        return ResponseEntity.ok("User registered successfully");
    }
}
