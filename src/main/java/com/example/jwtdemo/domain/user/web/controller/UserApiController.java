package com.example.jwtdemo.domain.user.web.controller;

import com.example.jwtdemo.domain.user.config.JWTUtil;
import com.example.jwtdemo.domain.user.domain.User;
import com.example.jwtdemo.domain.user.service.UserService;
import com.example.jwtdemo.domain.user.web.dto.req.UserLoginRequestDto;
import com.example.jwtdemo.domain.user.web.dto.req.UserSaveReqDto;
import com.example.jwtdemo.domain.user.web.dto.res.UserTokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;

import static java.lang.String.format;

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
    public ResponseEntity<UserTokenInfo> login(
            @RequestBody UserLoginRequestDto userLoginRequestDto){

        User user = userService.findByUserIdAndPassword(userLoginRequestDto.getUserId(), userLoginRequestDto.getPassword());
        String token = JWTUtil.makeAuthToken(user);
        String refresh_token = JWTUtil.makeRefreshToken(user);

        return ResponseEntity.ok(
                UserTokenInfo.builder()
                        .authToken(token)
                        .refreshToken(refresh_token)
                        .build());

    }


}
