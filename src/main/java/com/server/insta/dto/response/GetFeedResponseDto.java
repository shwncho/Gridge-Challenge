package com.server.insta.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetFeedResponseDto {

    @Schema(description = "게시물의 좋아요 개수")
    private int likeCount;

    @Schema(description = "게시물의 댓글 개수")
    private int commentCount;

    @Schema(description = "게시물 상세정보")
    private GetPostResponseDto getPostResponseDto;
}
