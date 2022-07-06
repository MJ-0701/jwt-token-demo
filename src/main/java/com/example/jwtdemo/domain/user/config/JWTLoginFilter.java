package com.example.jwtdemo.domain.user.config;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.jwtdemo.domain.user.domain.User;
import com.example.jwtdemo.domain.user.service.UserService;
import com.example.jwtdemo.domain.user.web.dto.req.UserLoginRequestDto;
import com.example.jwtdemo.domain.user.web.dto.res.TokenVerifyResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter { // Username,Password 를 받아서 유효하면 인증 토큰을 내려주는 필터.

    private ObjectMapper objectMapper = new ObjectMapper();
    private UserService userService;



    public JWTLoginFilter(AuthenticationManager authenticationManager, UserService userService){
        super(authenticationManager);
        this.userService = userService;
        setFilterProcessesUrl("/login");
    }


    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException { // 사용자 인증 처리

        UserLoginRequestDto userLogin = objectMapper.readValue(request.getInputStream(), UserLoginRequestDto.class);
        if(userLogin.getRefreshToken() == null){
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken( // 토큰  인증되기 전이기 때문에  Authorities null
                    userLogin.getUserId(), userLogin.getPassword(),null
            );
            // user details...
            return getAuthenticationManager().authenticate(token); // getAuthenticationManager 토큰 검증 요청
        }else{
            TokenVerifyResult verify = JWTUtil.verify(userLogin.getRefreshToken());
            if(verify.isSuccess()){
                User user = (User) userService.loadUserByUsername(verify.getUserId());
                return new UsernamePasswordAuthenticationToken(
                        user,user.getAuthorities()
                );
            }else{
                throw new TokenExpiredException("refresh token expired");
            }
        }


    }

    @Override
    protected void successfulAuthentication( // 검증이 제대로 됐다면 해당 메소드가 호출됨.
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
//        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer" + JWTUtil.makeAuthToken(user)); // 토큰 발행 ->  Bearer 토큰이라고 명시해줌(규약)
//        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE); // json 값을 받기위한 설정.
//        response.getOutputStream().write(objectMapper.writeValueAsBytes(user));

        response.setHeader("auth_token", JWTUtil.makeAuthToken(user));
        response.setHeader("refresh_token", JWTUtil.makeRefreshToken(user)); //
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().write(objectMapper.writeValueAsBytes(user));
    }
}
