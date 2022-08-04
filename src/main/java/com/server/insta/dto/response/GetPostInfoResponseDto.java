package com.server.insta.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetPostInfoResponseDto {

    @Schema(description = "게시물 번호(게시물 db id)")
    private Long postId;

    @Schema(description = "게시물 설명")
    private String caption;

    @Schema(description = "게시물 이미지 or 동영상 URL")
    private List<String> medias;

    @Schema(description = "게시물 태그들")
    private List<String> tags;

    @Schema(description = "게시물 좋아요한 유저 username들")
    private List<String> likeUsername;

    @Schema(description = "게시물이 신고 당한 횟수")
    private int reportCount;
}
