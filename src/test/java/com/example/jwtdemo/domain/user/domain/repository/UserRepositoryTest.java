package com.example.jwtdemo.domain.user.domain.repository;

import com.example.jwtdemo.domain.user.domain.Role;
import com.example.jwtdemo.domain.user.domain.User;
import com.example.jwtdemo.domain.user.domain.UserAddress;
import com.example.jwtdemo.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    void before(){
        userRepository.deleteAll();
        UserAddress userAddress = new UserAddress();

        User user = userService.save(User
                .builder()
                .userId("user1")
                .password("1111")
                .role(Role.USER)
                .enabled(true)
//                .userAddress("경기도")
                .build());

        userService.addAuthority(user.getIdx(), "ROLE_USER");
    }




}