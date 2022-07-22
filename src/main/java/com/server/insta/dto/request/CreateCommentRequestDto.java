package com.server.insta.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CreateCommentRequestDto {

    @Schema(description = "댓글 내용")
    private String content;

    @Schema(description = "최상위 댓글, parentId있으면 넣어주고, 없으면 최상위 댓글이라는 의미 이므로 null로 넘겨주시면 됩니다.")
    private Long parentId;
}
