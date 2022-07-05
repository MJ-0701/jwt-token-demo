package com.example.jwtdemo.domain.user.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.jwtdemo.domain.user.domain.User;
import com.example.jwtdemo.domain.user.web.dto.res.TokenVerifyResult;

import java.time.Instant;

public class JWTUtil {

    private static final Algorithm ALGORITHM = Algorithm.HMAC256("jack");
    private static final long AUTH_TIME = 20*60; // 20분
    private static final long REFRESH_TIME = 60*60*24*7; // 1주일

    // 토큰 생성
    public static String makeAuthToken(User user){
        return JWT.create()
                .withSubject(user.getUserId())
                .withClaim("exp", Instant.now().getEpochSecond()+AUTH_TIME)
                .sign(ALGORITHM);
    }

    // 리프레시 토큰 생성
    public static String makeRefreshToken(User user){
        return JWT.create()
                .withSubject(user.getUserId())
                .withClaim("exp", Instant.now().getEpochSecond()+REFRESH_TIME)
                .sign(ALGORITHM);
    }

    public static TokenVerifyResult verify(String token){
        try {
            DecodedJWT verify = JWT.require(ALGORITHM).build().verify(token);
            return TokenVerifyResult.builder()
                    .success(true)
                    .userId(verify.getSubject())
                    .build();
        }catch (Exception e){
            DecodedJWT verify = JWT.require(ALGORITHM).build().verify(token);
            return TokenVerifyResult.builder()
                    .success(false)
                    .userId(verify.getSubject())
                    .build();
        }

    }



}
