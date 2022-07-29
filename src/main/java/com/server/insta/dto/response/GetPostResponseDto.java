package com.server.insta.dto.response;

import com.server.insta.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetPostResponseDto {

    @Schema(description = "게시물 작성자 db id")
    private Long userId;

    @Schema(description = "게시물 작성자 사용자 이름")
    private String nickname;

    @Schema(description = "게시물 작성자 프로필 사진")
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

    @Schema(description = "게시물 작성시간")
    private String createdPost;

}
