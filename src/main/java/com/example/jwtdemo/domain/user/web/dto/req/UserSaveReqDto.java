package com.example.jwtdemo.domain.user.web.dto.req;

import com.example.jwtdemo.domain.user.domain.Role;
import com.example.jwtdemo.domain.user.domain.User;
import com.example.jwtdemo.domain.user.domain.UserAddress;
import com.example.jwtdemo.domain.user.domain.UserAuthority;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

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

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_idx", foreignKey = @ForeignKey(name = "user_idx"))
    private Set<UserAuthority> authorities;

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
                .authorities(authorities)
//                .companyAddress(companyAddress)
//                .userAddress(userAddress)
                .build();
    }


}
