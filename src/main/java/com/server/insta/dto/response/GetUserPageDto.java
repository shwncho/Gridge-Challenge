package com.server.insta.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetUserPageDto {

    @Schema(description = "사용자 이름")
    private String username;

    @Schema(description = "프로필 사진")
    private String profileImgUrl;

    @Schema(description = "유저 실명")
    private String name;

    @Schema(description = "유저 소개")
    private String introduce;

    @Schema(description = "유저 웹사이트")
    private String website;

    @Schema(description = "게시물 개수")
    private int postCount;

    @Schema(description = "팔로워 수")
    private int followerCount;

    @Schema(description = "팔로잉 수")
    private int followingCount;

    @Schema(description = "프로필 피드목록")
    private List<GetFeedResponseDto> getFeedResponseDtos;
}
