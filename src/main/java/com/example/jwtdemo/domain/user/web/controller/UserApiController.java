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

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Long> create(@RequestBody @Valid UserSaveReqDto reqDto){

        return ResponseEntity.ok(userService.createUser(reqDto));
    }

    @PostMapping("/save")
    public ResponseEntity<User> save(@RequestBody @Valid UserSaveReqDto reqDto){
        User user = userService.save(reqDto.toEntity());
        userService.addAuthority(user.getIdx(), "ROLE_USER");
        return ResponseEntity.ok(user);
    }

    // 토큰 생성
    @PostMapping("/login")
    public ResponseEntity<UserTokenInfo> login(@RequestBody UserLoginRequestDto userLoginRequestDto){
        // 현재 문제점 HttpServletRequest 요청이 가지 않음.

       User user = userService.findByUserIdAndPassword(userLoginRequestDto.getUserId(), userLoginRequestDto.getPassword());
       String token = JWTUtil.makeAuthToken(user);

       return ResponseEntity.ok( // 사실상 검증되지 않은 토큰 발행중
               UserTokenInfo.builder()
               .authToken(token)
               .refreshToken(token)
               .build());
    }


}
