package com.server.insta.dto.response;

import com.server.insta.config.Entity.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Builder
public class GetSearchPostsResponseDto {

    @Schema(description = "작성자 이름")
    private String name;

    @Schema(description = "작성자 아이디")
    private String username;

    @Schema(description = "피드 생성 일자")
    private String createdAt;

    @Schema(description = "게시물 상태")
    @Enumerated(EnumType.STRING)
    private Status status;
}
