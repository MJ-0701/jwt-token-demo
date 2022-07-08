package com.example.jwtdemo.domain.user.web.dto.res;

import com.example.jwtdemo.domain.user.domain.User;
import com.example.jwtdemo.domain.user.domain.UserAddress;
import lombok.Getter;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;

@Getter
public class UserInfoResponseDto {

    private String name;

    private String email;

    @Embedded
    @AttributeOverride(name = "zoneCode", column = @Column(name = "user_zone_code", nullable = false))
    @AttributeOverride(name = "address", column = @Column(name = "user_address", nullable = false))
    @Column(nullable = false)
    private UserAddress userAddress;

    public UserInfoResponseDto(User entity){
        this.name = entity.getUsername();
        this.email = entity.getEmail();
        this.userAddress = entity.getUserAddress();
    }
}
