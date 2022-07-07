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

    private String addressEnglish;

    private String addressType;

    private String userSelectedType;

    private String noSelected;

    private String userLanguageType;

    private String roadAddress;

    private String roadAddressEnglish;

    private String jibunAddress;

    private String jibunAddressEnglish;

    private String autoRoadAddress;

    private String autoRoadAddressEnglish;

    private String buildingCode;

    private String buildingName;

    private String apartment;

    private String sido;

    private String sidoEnglish;

    private String sigungu;

    private String sigunguEnglish;

    private String sigunguCode;

    private String roadnameCode;

    private String bcode;

    private String roadname;

    private String roadnameEnglish;

    private String bname;

    private String bnameEnglish;

    private String bname1;

    private String bname1English;

    private String bname2;

    private String bname2English;

    private String hname;

    @Comment("사용자가 입력한 검색어")
    private String query;
}
