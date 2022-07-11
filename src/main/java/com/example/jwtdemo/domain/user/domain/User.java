package com.example.jwtdemo.domain.user.domain;

import com.example.jwtdemo.domain.user.web.dto.req.UserSaveReqDto;
import com.example.jwtdemo.global.entity.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_table")
@Setter
public class User extends BaseTimeEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_idx")
    private Long idx;

    private String userName;

//    @Column(unique = true)
    private String userId;

    @Email
    @Comment("이메일")
//    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private boolean enabled;


//    public String getRoleKey(){
//        return this.role.getKey();
//    }


    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_idx", foreignKey = @ForeignKey(name = "user_idx"))
    private Set<UserAuthority> authorities;


    @Embedded
    @AttributeOverride(name = "zoneCode", column = @Column(name = "user_zone_code", nullable = false))
    @AttributeOverride(name = "address", column = @Column(name = "user_address", nullable = false))
    @Column(nullable = false)
    private UserAddress userAddress;

//    @Embedded
//    @AttributeOverride(name = "zoneCode", column = @Column(name = "company_zone_code"))
//    @AttributeOverride(name = "address", column = @Column(name = "company_address"))
//    private UserAddress companyAddress;

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return enabled;
    }

    public UserSaveReqDto toDto(){
        return UserSaveReqDto.builder()
                .userName(userName)
                .userId(userId)
                .email(email)
                .password(password)
                .build();
    }

}
