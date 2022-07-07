package com.example.jwtdemo.domain.user.web.controller;

import com.example.jwtdemo.WebIntegrationTest;
import com.example.jwtdemo.domain.user.domain.Role;
import com.example.jwtdemo.domain.user.domain.User;
import com.example.jwtdemo.domain.user.domain.repository.UserRepository;
import com.example.jwtdemo.domain.user.service.UserService;
import com.example.jwtdemo.domain.user.web.dto.req.UserLoginRequestDto;
import com.example.jwtdemo.domain.user.web.dto.res.UserTokenInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest extends WebIntegrationTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @BeforeEach
    void before(){
        userRepository.deleteAll();

        User user = userService.save(User
                .builder()
                .userId("user1")
                .password("1111")
                .role(Role.USER)
                .enabled(true)
                .build());

        userService.addAuthority(user.getIdx(), "ROLE_USER");
    }

    private UserTokenInfo getToken(){
        RestTemplate client = new RestTemplate();
        HttpEntity<UserLoginRequestDto> body = new HttpEntity<>(
                UserLoginRequestDto.builder().userId("user1").password("1111").build()
        ); // 로그인 유효성 검사
        ResponseEntity<User> resp1 = client.exchange(uri("/login"), HttpMethod.POST, body, User.class); // 실제 토큰 생성 부분 -> 검사 후 id/pwd가 유효 하다면 토큰생성
        return UserTokenInfo.builder().authToken(resp1.getHeaders().get("auth_token").get(0))
                .refreshToken(resp1.getHeaders().get("refresh_token").get(0))
                .build();
    }

    private UserTokenInfo refreshToken(String refreshToken){
        RestTemplate client = new RestTemplate();
        HttpEntity<UserLoginRequestDto> body = new HttpEntity<>(
                UserLoginRequestDto.builder().refreshToken(refreshToken).build()
        );
        ResponseEntity<User> resp1 = client.exchange(uri("/login"), HttpMethod.POST, body, User.class);
        return UserTokenInfo.builder().authToken(resp1.getHeaders().get("auth_token").get(0))
                .refreshToken(resp1.getHeaders().get("refresh_token").get(0))
                .build();
    }

    @DisplayName("1. hello 메시지 접근 가능 확인")
    @Test
    void test_1(){
        UserTokenInfo token = getToken();

        RestTemplate client = new RestTemplate();
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.AUTHORIZATION, "Bearer "+token.getAuthToken());
        HttpEntity body = new HttpEntity<>(null, header);

        ResponseEntity<String> resp2 = client.exchange(uri("/greeting"), HttpMethod.GET, body, String.class);

        assertEquals("hello", resp2.getBody());

    }

    @DisplayName("2. 토큰 만료 테스트 ")
    @Test
    void test_2() throws InterruptedException {
        UserTokenInfo token = getToken();

        Thread.sleep(3000);
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.AUTHORIZATION, "Bearer "+token.getAuthToken());
        RestTemplate client = new RestTemplate();
        assertThrows(Exception.class, ()->{
            HttpEntity body = new HttpEntity<>(null, header);
            ResponseEntity<String> resp2 = client.exchange(uri("/greeting"), HttpMethod.GET, body, String.class);
        });

        token = refreshToken(token.getRefreshToken());
        HttpHeaders header2 = new HttpHeaders(); // 토큰을 다시 받아왔기 대문에 헤더를 다시 만든다.
        header2.add(HttpHeaders.AUTHORIZATION, "Bearer "+token.getAuthToken());
        HttpEntity body = new HttpEntity<>(null, header2);
        ResponseEntity<String> resp3 = client.exchange(uri("/greeting"), HttpMethod.GET, body, String.class);

        assertEquals("hello", resp3.getBody());
    }



}