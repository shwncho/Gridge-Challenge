package com.server.insta.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetFeedResponseDto {

    @Schema(description = "게시물 db id")
    private Long postId;

    @Schema(description = "게시물 상세정보")
    private GetPostResponseDto getPostResponseDto;


}
