package com.server.insta.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CreateCommentRequestDto {

    @Schema(description = "댓글 내용")
    private String content;

    @Schema(description = "최상위 댓글이면 null로, 대댓글이면 어떤 댓글에 달고 싶은지 id값을 넘겨주세요.")
    private Long parentId;
}
