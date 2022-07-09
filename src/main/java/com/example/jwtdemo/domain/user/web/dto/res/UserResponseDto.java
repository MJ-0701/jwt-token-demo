package com.example.jwtdemo.domain.user.web.dto.res;

import com.example.jwtdemo.domain.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class UserResponseDto {

    private String userName;

    private String userId;

    private String email;

    public UserResponseDto(User entity){
        this.userName = entity.getUsername();
        this.userId = entity.getUserId();
        this.email = entity.getEmail();
    }

}
