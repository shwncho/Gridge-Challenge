package com.server.insta.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GetCommentsResponseDto {

    @Schema(description = "댓글 db id")
    private Long commentId;

    @Schema(description = "댓글 내용")
    private String content;

    @Schema(description = "댓글 작성자 db id")
    private Long userId;

    @Schema(description = "댓글 작성자 사용자 이름")
    private String nickname;

    @Schema(description = "댓글 작성자 프로필 사진")
    private String profileImgUrl;

    @Schema(description = "대댓글 목록")
    private List<GetCommentsResponseDto> children = new ArrayList<>();

    @Schema(description = "댓글 작성시간")
    private String createdComment;

    @Schema(description = "댓글 좋아요 개수")
    private int likeCount;

    @Builder
    public GetCommentsResponseDto(Long commentId, String content, Long userId, String nickname, String profileImgUrl, int likeCount){
        this.commentId = commentId;
        this.content = content;
        this.userId = userId;
        this.nickname = nickname;
        this.profileImgUrl = profileImgUrl;
        this.likeCount = likeCount;
    }


}
