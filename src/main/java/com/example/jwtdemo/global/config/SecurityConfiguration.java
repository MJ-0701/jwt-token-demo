package com.example.jwtdemo.global.config;

import com.example.jwtdemo.domain.user.config.JWTCheckFilter;
import com.example.jwtdemo.domain.user.config.JWTLoginFilter;
import com.example.jwtdemo.domain.user.domain.Role;
import com.example.jwtdemo.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserService userService; // 체크 필터에서 유저 정보를 직접 가져고 올 상황이 생길수 있음.

    @Bean
    PasswordEncoder passwordEncoder(){ // password encoder 작성 해야됨. 테스트성이라 현재는 그대로 사용
        return NoOpPasswordEncoder.getInstance();
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {

        JWTLoginFilter loginFilter = new JWTLoginFilter(authenticationManager(), userService); // 로그인을 처리하는 로그인 필터
        JWTCheckFilter checkFilter = new JWTCheckFilter(authenticationManager(), userService); // 로그인된 토큰을 매번 리퀘스트마다 체크해줄 체크 필터


        http.csrf().disable()
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용 안함 -> jwt 토큰을 사용하기 때문. -> 세션을 사용하지 않기 때문에 Authentication / Authorization 문제가 생김
                .authorizeRequests()
                .antMatchers("/api/v1/user/**").permitAll()
                .antMatchers("/api/v1/user/login").hasRole(Role.USER.name())
                .and()
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class) // 세션을 사용하지 않고 토큰을 사용하여 인증. -> 로그인 처리
                .addFilterAt(checkFilter, BasicAuthenticationFilter.class) // -> 토큰 검증
        ;
    }
}
