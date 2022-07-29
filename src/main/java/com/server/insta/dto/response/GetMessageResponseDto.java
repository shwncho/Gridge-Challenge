package com.server.insta.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GetMessageResponseDto {

    private String sender;

    private String content;

    private String createdAt;

}
