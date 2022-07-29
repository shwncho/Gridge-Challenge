package com.server.insta.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetFollowingResponseDto {

    @Schema(description = "유저 db id")
    private Long userId;

    @Schema(description = "사용자 이름")
    private String nickname;

    @Schema(description = "유저 프로필 이미지")
    private String profileImgUrl;

    @Schema(description = "유저 본인 소개")
    private String introduce;
}
