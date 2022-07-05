package com.example.jwtdemo.domain.user.config;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.jwtdemo.domain.user.domain.User;
import com.example.jwtdemo.domain.user.service.UserService;
import com.example.jwtdemo.domain.user.web.dto.req.UserLoginRequestDto;
import com.example.jwtdemo.domain.user.web.dto.res.TokenVerifyResult;
import com.fasterxml.jackson.databind.ObjectMapper;
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

public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

    private ObjectMapper objectMapper = new ObjectMapper();
    private UserService userService;

    public JWTLoginFilter(AuthenticationManager authenticationManager){
        super(authenticationManager);
        setFilterProcessesUrl("/login");
    }


    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        UserLoginRequestDto userLogin = objectMapper.readValue(request.getInputStream(), UserLoginRequestDto.class);
        if(userLogin.getRefreshToken() == null){
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    userLogin.getUserId(), userLogin.getPassword(),null
            );
            // user details...
            return getAuthenticationManager().authenticate(token);
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
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
//        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer" + JWTUtil.makeAuthToken(user));
//        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
//        response.getOutputStream().write(objectMapper.writeValueAsBytes(user));

        response.setHeader("auth_token", JWTUtil.makeAuthToken(user));
        response.setHeader("refresh_token", JWTUtil.makeRefreshToken(user));
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().write(objectMapper.writeValueAsBytes(user));
    }
}
