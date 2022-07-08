package com.example.jwtdemo.domain.user.web.controller;

import com.example.jwtdemo.domain.user.config.JWTUtil;
import com.example.jwtdemo.domain.user.domain.User;
import com.example.jwtdemo.domain.user.service.UserAddressService;
import com.example.jwtdemo.domain.user.service.UserService;
import com.example.jwtdemo.domain.user.web.dto.req.UserAddressRegisterDto;
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

    private final UserAddressService userAddressService;

    @PostMapping("/create")
    public ResponseEntity<Long> create(
            @RequestBody @Valid UserSaveReqDto reqDto,
            @RequestBody UserAddressRegisterDto registerDto
    ){

        return ResponseEntity.ok(userService.createUser(reqDto, registerDto));
    }

    @PostMapping("/save")
    public ResponseEntity<User> save(@RequestBody @Valid UserSaveReqDto reqDto){
        User user = userService.save(reqDto.toEntity());
        userService.addAuthority(user.getIdx(), "ROLE_USER");
        return ResponseEntity.ok(user);
    }

    // 로그인 -> 토큰생성
    @PostMapping("/login/client")
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

    @PostMapping("/login/mobile")
    public ResponseEntity<UserTokenInfo> mobileLoginTest(
            @RequestBody UserLoginRequestDto userLoginRequestDto
    ){

        ResponseEntity<User> user = ResponseEntity.ok(userService.findByUserIdAndPassword(userLoginRequestDto.getUserId(), userLoginRequestDto.getPassword()));

        return ResponseEntity.ok(UserTokenInfo
                .builder()
                .authToken(user.getHeaders().get("auth_token").get(0))
                .refreshToken(user.getHeaders().get("refresh_token").get(0))
                .build());
    }

    @PostMapping("/login/web-client")
    public ResponseEntity<User> webLogin(@RequestBody UserLoginRequestDto userLoginRequestDto){
        return ResponseEntity.ok(userService.findByUserIdAndPassword(userLoginRequestDto.getUserId(), userLoginRequestDto.getPassword()));

    }

    @PostMapping("/login/mobile-client")
    public ResponseEntity<User> mobileLogin(@RequestBody UserLoginRequestDto userLoginRequestDto){
        return ResponseEntity.ok(userService.findByUserIdAndPassword(userLoginRequestDto.getUserId(), userLoginRequestDto.getPassword()));
    }


}
