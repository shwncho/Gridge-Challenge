package com.server.insta.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.server.insta.config.Entity.Authority;
import com.server.insta.config.Entity.Provider;
import com.server.insta.config.Entity.Status;
import com.server.insta.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class GetUserInfoResponseDto {

    @Schema(description = "유저 db id")
    private Long userId;

    @Schema(description = "유저 email")
    private String email;

    @Schema(description = "유저 사용자 이름")
    private String username;

    @Schema(description = "유저 이름")
    private String name;

    @Schema(description = "유저 휴대폰 번호")
    private String phoneNumber;

    @Schema(description = "유저 프로필 사진")
    private String profileImgUrl;

    @Schema(description = "유저 소개")
    private String introduce;

    @Schema(description = "유저 웹사이트")
    private String website;

    @Enumerated(EnumType.STRING)
    @Schema(description = "유저 로그인 제공자")
    private Provider provider;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "유저 생일")
    private Date birth;

    @Enumerated(EnumType.STRING)
    @Schema(description = "유저 상태")
    private Status status;

    @Enumerated(EnumType.STRING)
    @Schema(description = "유저 권한")
    private Authority authority;

    @JsonProperty("isPublic")
    @Schema(description = "유저 계정 공개/비공개 여부")
    public boolean isPublic;
}
