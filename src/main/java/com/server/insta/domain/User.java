package com.server.insta.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.server.insta.config.Entity.Authority;
import com.server.insta.config.Entity.BaseTimeEntity;
import com.server.insta.config.Entity.Provider;
import com.server.insta.config.Entity.Status;
import com.server.insta.dto.response.GetFollowerResponseDto;
import com.server.insta.dto.response.GetFollowingResponseDto;
import com.server.insta.dto.response.GetLikeUsersResponseDto;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Getter
@Entity
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
    private String nickname;

    @NotNull
    private String name;

    private String phoneNumber;

    @Column(columnDefinition = "TEXT")
    private String profileImgUrl;

    private String introduce;

    private String website;

    @Enumerated(EnumType.STRING)
    private Provider provider; //kakao,google,facebook,normal

    @Temporal(TemporalType.DATE)
    private Date birth;

    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    //공개/비공개 계정
    public boolean isPublic;

    public void changePassword(String password){
        this.password = password;
    }

    public void openPublic(){
        this.isPublic = true;
    }

    public void closePublic(){
        this.isPublic = false;
    }


    public void deleteStatus(){
        this.status = Status.DELETED;
    }

    @Builder
    public User(String email, String password, String nickname, String name, String phoneNumber, String profileImgUrl,
                String introduce, String website, Provider provider, Date birth, boolean isPublic) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.profileImgUrl = profileImgUrl;
        this.introduce = introduce;
        this.website = website;
        this.provider = provider;
        this.birth = birth;
        this.isPublic = true;
        this.status = Status.ACTIVE;
        this.authority = Authority.ROLE_USER;
    }


    public GetFollowingResponseDto toFollowing(){
        return GetFollowingResponseDto.builder()
                .userId(id)
                .nickname(nickname)
                .profileImgUrl(profileImgUrl)
                .introduce(introduce)
                .build();
    }

    public GetFollowerResponseDto toFollower(){
        return GetFollowerResponseDto.builder()
                .userId(id)
                .nickname(nickname)
                .profileImgUrl(profileImgUrl)
                .introduce(introduce)
                .build();
    }

    public GetLikeUsersResponseDto toLikeUsers(){
        return GetLikeUsersResponseDto.builder()
                .userId(id)
                .nickname(nickname)
                .profileImgUrl(profileImgUrl)
                .introduce(introduce)
                .build();
    }
}
