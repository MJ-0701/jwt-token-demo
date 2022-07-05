package com.example.jwtdemo.domain.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user_authority")
@IdClass(UserAuthority.class)
@Data
public class UserAuthority implements GrantedAuthority {

    @Id
    private Long idx;

    @Id
    private String authority;
}
