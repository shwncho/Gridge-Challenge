package com.server.insta.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CreateCommentRequestDto {

    @NotBlank
    @Schema(description = "댓글 내용")
    @Size(max = 200, message = "최대 200자까지만 작성가능합니다.")
    private String content;

    @Schema(description = "새로운 댓글이면 null로, 대댓글이면 달고 싶은 댓글의 [첫 댓글 id]값을 넘겨주세요.")
    private Long parentId;
}
