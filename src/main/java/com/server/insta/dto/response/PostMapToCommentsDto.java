package com.server.insta.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PostMapToCommentsDto {

    @Schema(description = "게시물 작성자 db id")
    private Long userId;

    @Schema(description = "게시물 작성자 닉네임")
    private String nickName;

    @Schema(description = "게시물 작성자 프로필 사진")
    private String profileImgUrl;

    @Schema(description = "조회한 게시물 설명")
    private String caption;

    @Schema(description = "게시물 작성시간")
    private String createdPost;

    @Schema(description = "댓글 작성 리스트")
    private List<GetCommentsResponseDto> getCommentsResponseDtoList;
}
