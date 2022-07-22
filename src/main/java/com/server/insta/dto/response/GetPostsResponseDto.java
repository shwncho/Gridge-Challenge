package com.server.insta.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetPostsResponseDto {

    @Schema(description = "조회한 게시물 설명")
    private String caption;

    @Schema(description = "조회한 게시물 이미지")
    private List<String> medias;

    @Schema(description = "조회한 해시태그")
    private List<String> tags;
}
