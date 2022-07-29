package com.server.insta.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetChattingResponseDto {

    @Schema(description = "채팅 상대방 유저 db id")
    private Long otherUserId;

    @Schema(description = "채팅 상대방 프로필 사진")
    private String profileImgUrl;

    @Schema(description = "메세지 이력")
    private List<GetMessageResponseDto> getMessageResponseDtos;
}
