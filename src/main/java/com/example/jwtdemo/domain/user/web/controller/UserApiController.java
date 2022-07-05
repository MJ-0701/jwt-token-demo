package com.example.jwtdemo.domain.user.web.controller;

import com.example.jwtdemo.domain.user.config.JWTUtil;
import com.example.jwtdemo.domain.user.domain.User;
import com.example.jwtdemo.domain.user.domain.UserToken;
import com.example.jwtdemo.domain.user.service.UserService;
import com.example.jwtdemo.domain.user.web.dto.req.UserSaveReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // 토큰 생성
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserToken token){

        User user = userService.findByUserIdAndPassword(token.getUserId(), token.getPassword());

        String jwtToken = JWTUtil.makeAuthToken(user);

        System.out.println(JWTUtil.verify(jwtToken).getUserId());

        return ResponseEntity.ok(jwtToken);

    }


}
