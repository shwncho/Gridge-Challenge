package com.server.insta.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetUserPageDto {

    private String nickname;

    private String profileImgUrl;

    private String name;

    private int postCount;

    private int followerCount;

    private int followingCount;

    private List<GetFeedResponseDto> getFeedResponseDtos;
}
