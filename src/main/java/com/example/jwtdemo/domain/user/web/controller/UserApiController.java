package com.example.jwtdemo.domain.user.web.controller;

import com.example.jwtdemo.domain.user.domain.User;
import com.example.jwtdemo.domain.user.service.UserService;
import com.example.jwtdemo.domain.user.web.dto.req.UserLoginRequestDto;
import com.example.jwtdemo.domain.user.web.dto.req.UserSaveReqDto;
//import com.example.jwtdemo.global.config.JwtTokenProvider;
import com.example.jwtdemo.global.config.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

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

//        RestTemplate client = new RestTemplate();
//        HttpHeaders header = new HttpHeaders();
//        HttpEntity<UserLoginRequestDto> body = new HttpEntity<>(
//                UserLoginRequestDto
//                        .builder()
//                        .userId(loginRequestDto.getUserId())
//                        .password(loginRequestDto.getPassword())
//                        .build()
//        );
//
//        ResponseEntity<User> response = client.exchange(uri("/login"), HttpMethod.POST, body, User.class);
//
//        UserTokenInfo token = UserTokenInfo
//                .builder()
//                .authToken(response.getHeaders().get("auth_token").get(0))
//                .refreshToken(response.getHeaders().get("refresh_token").get(0))
//                .build();
//
//        header.add(HttpHeaders.AUTHORIZATION, "Bearer "+token.getAuthToken());
//        HttpEntity httpEntity = new HttpEntity<>(null, header);
//
//
//        return ResponseEntity.ok(httpEntity);


//        User user = userService.findByUserIdAndPassword(loginRequestDto.getUserId(), loginRequestDto.getPassword());
//        String userToken = JWTUtil.makeAuthToken(user);
//        return ResponseEntity.ok(userToken);

        User user = userService.findByUserIdAndPassword(loginRequestDto.getUserId(), loginRequestDto.getPassword());
        return ResponseEntity.ok(jwtTokenProvider.createToken(user.getUserId(), user.getRole()));
    }



    public URI uri(String path) {
        try {
            return new URI(format("http://localhost:%d%s", 8080, path));
        }catch(Exception ex){
            throw new IllegalArgumentException();
        }
    }




}
