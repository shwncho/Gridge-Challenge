package com.server.insta.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetLogsResponseDto {

    @Schema(description = "로그 db id")
    private Long logId;

    @Schema(description = "로그 내용")
    private String text;

    @Schema(description = "로그 생성일자")
    private String createdDate;
}
