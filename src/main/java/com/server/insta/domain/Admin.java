package com.server.insta.domain;

import com.server.insta.config.Entity.Authority;
import com.server.insta.config.Entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Long id;

    private String adminId;

    private String password;

    private String adminName;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Builder
    public Admin(String adminId, String password, String adminName) {
        this.adminId = adminId;
        this.password = password;
        this.adminName = adminName;
        this.authority = Authority.ROLE_ADMIN;
    }
}
