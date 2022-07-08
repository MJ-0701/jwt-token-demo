package com.example.jwtdemo.domain.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @OneToOne
    @JoinColumn(name = "user_idx", foreignKey = @ForeignKey(name = "user_idx"))
    private User user;

    private String zoneCode;

    private String address;

}
