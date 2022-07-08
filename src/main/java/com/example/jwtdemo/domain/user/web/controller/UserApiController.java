package com.example.jwtdemo.domain.user.web.controller;

import com.example.jwtdemo.domain.user.config.JWTUtil;
import com.example.jwtdemo.domain.user.domain.User;
import com.example.jwtdemo.domain.user.domain.UserAddress;
import com.example.jwtdemo.domain.user.service.UserService;
import com.example.jwtdemo.domain.user.web.dto.req.UserLoginRequestDto;
import com.example.jwtdemo.domain.user.web.dto.req.UserSaveReqDto;
import com.example.jwtdemo.domain.user.web.dto.res.UserInfoResponseDto;
import com.example.jwtdemo.domain.user.web.dto.res.UserTokenInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
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
import java.util.List;

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
    public ResponseEntity<UserTokenInfo> mobileLogin(
            @RequestBody UserLoginRequestDto userLoginRequestDto
    ){

        ResponseEntity<User> user = ResponseEntity.ok(userService.findByUserIdAndPassword(userLoginRequestDto.getUserId(), userLoginRequestDto.getPassword()));

        return ResponseEntity.ok(UserTokenInfo
                .builder()
                .authToken(user.getHeaders().get("auth_token").get(0))
                .refreshToken(user.getHeaders().get("refresh_token").get(0))
                .build());
    }

    @GetMapping("/user-info/{id}")
    public ResponseEntity<User> userInfo(@PathVariable Long id){

        return ResponseEntity.ok(userService.findByIdx(id));
    }

    @GetMapping("/find-address")
    public ResponseEntity<List<User>> findAddress(@RequestParam String address){
        System.out.println("확인 :" + address);
        System.out.println(userService.findByUserAddress(address));

        return ResponseEntity.ok(userService.findByUserAddress(address));

    }


}
