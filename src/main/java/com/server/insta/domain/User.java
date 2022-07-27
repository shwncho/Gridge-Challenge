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
    private String nickName;

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



    @Builder
    public User(String email, String name, String nickname, String password, String phoneNumber, Date birth) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.nickName = nickname;
        this.phoneNumber = phoneNumber;
        this.birth = birth;
        this.provider = Provider.NORMAL;
        this.status = Status.ACTIVE;
        this.authority = Authority.ROLE_USER;
    }

    public GetFollowingResponseDto toFollowing(){
        return GetFollowingResponseDto.builder()
                .userId(id)
                .nickName(nickName)
                .profileImgUrl(profileImgUrl)
                .introduce(introduce)
                .build();
    }

    public GetFollowerResponseDto toFollower(){
        return GetFollowerResponseDto.builder()
                .userId(id)
                .nickName(nickName)
                .profileImgUrl(profileImgUrl)
                .introduce(introduce)
                .build();
    }

    public GetLikeUsersResponseDto toLikeUsers(){
        return GetLikeUsersResponseDto.builder()
                .userId(id)
                .nickName(nickName)
                .profileImgUrl(profileImgUrl)
                .introduce(introduce)
                .build();
    }
}
