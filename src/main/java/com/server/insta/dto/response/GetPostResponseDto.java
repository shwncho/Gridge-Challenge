package com.server.insta.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetPostResponseDto {

    @Schema(description = "게시물 주인의 닉네임")
   private String nickName;

    @Schema(description = "게시물 주인의 프로필 사진")
    private String profileImgUrl;

    @Schema(description = "조회한 게시물 설명")
    private String caption;

    @Schema(description = "게시물 좋아요 개수")
    private int likeCount;

    @Schema(description = "게시물 댓글 개수")
    private int commentCount;

    @Schema(description = "조회한 게시물 이미지")
    private List<String> medias;

    @Schema(description = "조회한 해시태그")
    private List<String> tags;
}
