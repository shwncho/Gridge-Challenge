package com.server.insta.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
public class GetMessageResponseDto {

    @Schema(description = "메세지 db id")
    private Long messageId;

    @Schema(description = "메세지를 보내는 유저")
    private String sender;

    @Schema(description = "메세지 내용")
    private String content;

    @Schema(description = "메세지 생성시간")
    private String createdAt;

}
