package com.example.jwtdemo.domain.user.web.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
@Data
public class UserTokenInfo {
    private String authToken;
    private String refreshToken;
    private Long expTime;
}
