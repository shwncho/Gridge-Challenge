package com.server.insta.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.server.insta.config.Entity.Authority;
import com.server.insta.config.Entity.BaseTimeEntity;
import com.server.insta.config.Entity.Provider;
import com.server.insta.config.Entity.Status;
import com.server.insta.dto.response.GetFollowerResponseDto;
import com.server.insta.dto.response.GetFollowingResponseDto;
import com.server.insta.dto.response.GetLikeUsersResponseDto;
import com.server.insta.dto.response.GetSearchUsersResponseDto;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
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

    //소셜 로그인 email 용도
    private String email;

    @Column(length = 20)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)  // 패스워드는 응답값에 포함하지 않는다.
    private String password;


    @Column(length = 20)
    private String name;

    @Column(length = 11)
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

    //개인정보동의 1년주기 체크용도
    private LocalDateTime scheduler;

    // 이름 14일 주기 체크 용도
    private LocalDateTime updateNameDate;
    // 사용자 이름 14일 주기 체크 용도
    private LocalDateTime updateUsernameDate;

    private int nameCount;

    private int usernameCount;


    //공개/비공개 계정
    public boolean isPublic;

    public void changeProfile(String profileImgUrl, String name, String username, String website, String introduce){
        this.profileImgUrl = profileImgUrl;
        this.name = name;
        this.username = username;
        this.website = website;
        this.introduce = introduce;
    }

    public void changeUpdateNameDate(){
        this.updateNameDate = LocalDateTime.now();
        this.nameCount=1;
    }
    public void addNameCount(){ this.nameCount++;}
    public void addUsernameCount(){ this.usernameCount++;}

    public void changeUpdateUsernameDate(){
        this.updateUsernameDate = LocalDateTime.now();
        this.usernameCount=1;
    }

    public void changePassword(String password){
        this.password = password;
    }

    public void openPublic(){
        this.isPublic = true;
    }

    public void closePublic(){
        this.isPublic = false;
    }

    public void blockStatus(){ this.status = Status.BLOCK; }

    public void deleteStatus(){
        this.status = Status.DELETED;
    }

    public void changeAuthority(){
        this.authority = Authority.ROLE_ADMIN;
    }

    public void resetScheduler(){
        this.scheduler = LocalDateTime.now();
    }

    @Builder
    public User(String email, String username, String password, String name, String phoneNumber, String profileImgUrl,
                String introduce, String website, Provider provider, Date birth, boolean isPublic, LocalDateTime scheduler) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.profileImgUrl = profileImgUrl;
        this.introduce = introduce;
        this.website = website;
        this.provider = provider;
        this.birth = birth;
        this.isPublic = isPublic;
        this.status = Status.ACTIVE;
        this.authority = Authority.ROLE_USER;
        this.scheduler = scheduler;
    }
}
