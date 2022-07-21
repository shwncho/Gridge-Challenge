package com.server.insta.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FollowingResponseDto {

    private Long userId;

    private String profileImgUrl;

    private String introduce;
}
