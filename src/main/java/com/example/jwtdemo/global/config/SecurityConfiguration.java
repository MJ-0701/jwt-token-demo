package com.example.jwtdemo.global.config;

import com.example.jwtdemo.domain.user.config.JwtAuthenticationFilter;
import com.example.jwtdemo.domain.user.config.JwtTokenProvider;
import com.example.jwtdemo.domain.user.domain.Role;
import com.example.jwtdemo.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserService userService; // 체크 필터에서 유저 정보를 직접 가져고 올 상황이 생길수 있음.
    private final JwtTokenProvider jwtTokenProvider;

    // 암호화에 필요한 Password Encoder Bean 등록.
    @Bean
    PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        return NoOpPasswordEncoder.getInstance();
    }

    // AuthenticationManager Bean 등록


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용 안함 -> jwt 토큰을 사용하기 때문. -> 세션을 사용하지 않기 때문에 Authentication / Authorization 문제가 생김
                .authorizeRequests()
                .antMatchers("/api/v1/user/**").permitAll()
                .antMatchers("/greeting").hasRole(Role.USER.name())
                .antMatchers("/api/v1/user/login").hasRole(Role.USER.name())
                .and()
                .addFilterAt(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
        ;
    }
}
