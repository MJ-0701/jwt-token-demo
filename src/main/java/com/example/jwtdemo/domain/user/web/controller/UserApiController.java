package com.example.jwtdemo.domain.user.web.controller;

import com.example.jwtdemo.domain.user.config.JWTUtil;
import com.example.jwtdemo.domain.user.domain.User;
import com.example.jwtdemo.domain.user.service.UserService;
import com.example.jwtdemo.domain.user.web.dto.req.UserLoginRequestDto;
import com.example.jwtdemo.domain.user.web.dto.req.UserSaveReqDto;
import com.example.jwtdemo.domain.user.web.dto.res.UserTokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;


    @PostMapping("/save")
    public ResponseEntity<User> save(@RequestBody @Valid UserSaveReqDto reqDto){
        User user = userService.save(reqDto.toEntity());
        userService.addAuthority(user.getIdx(), "ROLE_USER");
        return ResponseEntity.ok(user);
    }
    @PostMapping("/create")
    public ResponseEntity<Long> create(@RequestBody @Valid UserSaveReqDto reqDto){

        return ResponseEntity.ok(userService.createUser(reqDto));
    }

    // 토큰 생성
    @PostMapping("/login")
    public ResponseEntity<UserTokenInfo> login(@RequestBody UserLoginRequestDto userLoginRequestDto){

        HttpEntity<UserLoginRequestDto> httpBody = new HttpEntity<>(
                UserLoginRequestDto.builder()
                        .userId(userLoginRequestDto.getUserId())
                        .password(userLoginRequestDto.getPassword())
                        .build()
        );
        ResponseEntity<User> user = ResponseEntity.ok(userService.findByUserIdAndPassword(userLoginRequestDto.getUserId(), userLoginRequestDto.getPassword()));

        UserTokenInfo token = UserTokenInfo
                .builder()
                .authToken(user.getHeaders().get("auth_token").get(0))
                .refreshToken(user.getHeaders().get("refresh_token").get(0))
                .build();

        return ResponseEntity.ok(token);


    }


}
