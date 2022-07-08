package com.example.jwtdemo.domain.user.web.dto.req;

import com.example.jwtdemo.domain.user.domain.Role;
import com.example.jwtdemo.domain.user.domain.User;
import com.example.jwtdemo.domain.user.domain.UserAddress;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSaveReqDto {

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("user_id")
    private String userId;

    private String email;

    private String password;

    @JsonProperty("user_address")
    private UserAddress userAddress;

    @JsonProperty("company_address")
//    private UserAddress companyAddress;





    public User toEntity(){
        return User.builder()
                .userName(userName)
                .userId(userId)
                .email(email)
                .password(password)
                .role(Role.USER)
                .enabled(true)
                .userAddress(userAddress)
//                .companyAddress(companyAddress)
                .build();
    }


}
