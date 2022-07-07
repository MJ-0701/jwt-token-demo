package com.example.jwtdemo.domain.user.web.controller;

import com.example.jwtdemo.domain.user.domain.User;
import com.example.jwtdemo.domain.user.service.UserService;
import com.example.jwtdemo.domain.user.web.dto.req.UserLoginRequestDto;
import com.example.jwtdemo.domain.user.web.dto.req.UserSaveReqDto;
//import com.example.jwtdemo.domain.user.config.JwtTokenProvider;
import com.example.jwtdemo.domain.user.config.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static java.lang.String.format;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    private final JwtTokenProvider jwtTokenProvider;



    @PostMapping("/save")
    public ResponseEntity<User> save(@RequestBody @Valid UserSaveReqDto reqDto){
        User user = userService.save(reqDto.toEntity());
        userService.addAuthority(user.getIdx(), "ROLE_USER");
        return ResponseEntity.ok(user);
    }

    @PostMapping("/create")
    public ResponseEntity<Long> create(@RequestBody @Valid UserSaveReqDto reqDto){

        userService.addAuthority(reqDto.toEntity().getIdx(), "ROLE_USER");

        return ResponseEntity.ok(userService.createUser(reqDto));
    }

    // 토큰 생성
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginRequestDto loginRequestDto){

        User user = userService.findByUserIdAndPassword(loginRequestDto.getUserId(), loginRequestDto.getPassword());
        return ResponseEntity.ok(jwtTokenProvider.createToken(user.getUserId(), user.getRole()));
    }





}
