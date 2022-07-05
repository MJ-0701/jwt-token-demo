package com.example.jwtdemo.global.config;

import com.example.jwtdemo.domain.user.config.JWTCheckFilter;
import com.example.jwtdemo.domain.user.config.JWTLoginFilter;
import com.example.jwtdemo.domain.user.domain.Role;
import com.example.jwtdemo.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    @Bean
    PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {

        JWTLoginFilter loginFilter = new JWTLoginFilter(authenticationManager());
        JWTCheckFilter checkFilter = new JWTCheckFilter(authenticationManager(), userService);


        http.csrf().disable()
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeRequests()
                .antMatchers("/api/v1/user/**").permitAll()
                .antMatchers("/api/v1/user/login").hasRole(Role.USER.name())
                .and()
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(checkFilter, BasicAuthenticationFilter.class);
    }
}
