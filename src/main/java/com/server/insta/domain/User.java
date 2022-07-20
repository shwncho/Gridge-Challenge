package com.server.insta.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.server.insta.config.Entity.BaseTimeEntity;
import com.server.insta.config.Entity.Status;
import com.server.insta.domain.Authority;
import com.server.insta.domain.Post;
import com.server.insta.dto.response.FollowerResponseDto;
import com.server.insta.dto.response.FollowingResponseDto;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotNull
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)  // 패스워드는 응답값에 포함하지 않는다.
    private String password;

    @NotNull
    private String nickName;

    @NotNull
    private String username;

    private String phoneNumber;

    @Column(columnDefinition = "TEXT")
    private String profileImgUrl;

    private String introduce;

    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Authority authority;



    @Builder
    public User(String email, String username, String nickname, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.nickName = nickname;
        this.status = Status.ACTIVE;
        this.authority = Authority.ROLE_USER;
    }

    @Builder
    public FollowingResponseDto toFollowing(){
        return FollowingResponseDto.builder()
                .userId(id)
                .profileImgUrl(profileImgUrl)
                .introduce(introduce)
                .build();
    }

    @Builder
    public FollowerResponseDto toFollower(){
        return FollowerResponseDto.builder()
                .userId(id)
                .profileImgUrl(profileImgUrl)
                .introduce(introduce)
                .build();
    }
}
