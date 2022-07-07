package com.example.jwtdemo.domain.user.web.dto.req;

import com.example.jwtdemo.domain.user.domain.User;
import com.example.jwtdemo.domain.user.domain.UserAddress;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAddressRegisterDto {

    private String address;

    private String zoneCode;

    private List<User> userList;

    public UserAddress toEntity(){
        return UserAddress
                .builder()
                .address(address)
                .zoneCode(zoneCode)
                .userList(userList)
                .build();
    }
}
