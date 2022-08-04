package com.server.insta.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.server.insta.config.Entity.Authority;
import com.server.insta.config.Entity.Provider;
import com.server.insta.config.Entity.Status;
import com.server.insta.domain.Post;
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

    private Long userId;

    private String email;

    private String username;

    private String name;

    private String phoneNumber;

    private String profileImgUrl;

    private String introduce;

    private String website;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birth;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @JsonProperty("isPublic")
    public boolean isPublic;
}
