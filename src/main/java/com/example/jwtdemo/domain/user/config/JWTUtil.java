package com.example.jwtdemo.domain.user.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.jwtdemo.domain.user.domain.User;
import com.example.jwtdemo.domain.user.web.dto.res.TokenVerifyResult;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

public class JWTUtil {

    private static final Algorithm ALGORITHM = Algorithm.HMAC256("spring-boot-jwt");
    private static final long AUTH_TIME = 20*60; // 20분

//    private static final long AUTH_TIME = 2;
    private static final long REFRESH_TIME = 60*60*24*7; // 1주일

    // 토큰 생성
    public static String makeAuthToken(User user){
        return JWT.create()
                .withSubject(user.getUserId())
                .withClaim("role", user.getRoleKey())
                .withClaim("exp", Instant.now().getEpochSecond()+AUTH_TIME) // 토큰 유효 시간 -> Date 클래스 사용 안하고
                .sign(ALGORITHM);
    }

    // 리프레시 토큰 생성
    public static String makeRefreshToken(User user){
        return JWT.create()
                .withSubject(user.getUserId())
                .withClaim("role", user.getRoleKey())
                .withClaim("exp", Instant.now().getEpochSecond()+REFRESH_TIME)
                .sign(ALGORITHM);
    }

    public static TokenVerifyResult verify(String token){ // 토큰 유효성 검사
        try {
            DecodedJWT verify = JWT.require(ALGORITHM).build().verify(token);
            return TokenVerifyResult.builder()
                    .success(true) // 유효 하다면 성공
                    .userId(verify.getSubject())
                    .build();
        }catch (Exception e){
            DecodedJWT decode = JWT.require(ALGORITHM).build().verify(token);
            return TokenVerifyResult.builder()
                    .success(false) // 유효 하지 않다면 실패
                    .userId(decode.getSubject()) // 누가 요청했는지
                    .build();
        }

    }

    public String resolveToken(HttpServletRequest request){
        return request.getHeader("X-AUTH-TOKEN");
    }



}
