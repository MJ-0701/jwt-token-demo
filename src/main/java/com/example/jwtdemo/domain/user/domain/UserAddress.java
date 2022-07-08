package com.example.jwtdemo.domain.user.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Embeddable
public class UserAddress {

    @JsonProperty("zone_code") // 안해줄시 json 데이터 못 받음.
    private String zoneCode;

    @JsonProperty("address")
    private String address;
    
}
