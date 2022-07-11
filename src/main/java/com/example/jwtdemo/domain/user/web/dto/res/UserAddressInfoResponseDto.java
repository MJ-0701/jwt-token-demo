package com.example.jwtdemo.domain.user.web.dto.res;

import com.example.jwtdemo.domain.user.domain.User;
import com.example.jwtdemo.domain.user.domain.UserAddress;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UserAddressInfoResponseDto {

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("user_address")
    public UserAddress userAddress;

    public UserAddressInfoResponseDto (User entity){
        this.userId = entity.getUserId();
        this.userName = entity.getUsername();
        this.userAddress = entity.getUserAddress();
    }
}
