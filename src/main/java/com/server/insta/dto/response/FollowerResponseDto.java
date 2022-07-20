package com.server.insta.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FollowerResponseDto {

    private Long userId;

    private String profileImgUrl;

    private String introduce;
}
