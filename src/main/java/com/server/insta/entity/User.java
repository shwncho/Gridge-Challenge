package com.server.insta.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.server.insta.config.Entity.BaseTimeEntity;
import com.server.insta.config.Entity.Status;
import lombok.*;

import javax.persistence.*;


@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String nickName;

    private String phoneNumber;

    private String profileImgUrl;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Builder
    public User(String email, String password, String nickName, String phoneNumber, String profileImgUrl) {
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
        this.profileImgUrl = profileImgUrl;
        this.status = Status.ACTIVE;
        this.authority = Authority.ROLE_USER;
    }
}
