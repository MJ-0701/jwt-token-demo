package com.example.jwtdemo.domain.user.web.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserTokenInfo {

    private String accessToken;

    private String refreshToken;

    private Long expTime;
}
