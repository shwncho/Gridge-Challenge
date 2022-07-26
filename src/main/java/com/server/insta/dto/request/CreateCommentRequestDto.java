package com.server.insta.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CreateCommentRequestDto {

    @Schema(description = "댓글 내용")
    private String content;

    @Schema(description = "첫 댓글이면 null로, 대댓글이면 달고 싶은 댓글의 [첫 댓글 id]값을 넘겨주세요.")
    private Long parentId;
}
