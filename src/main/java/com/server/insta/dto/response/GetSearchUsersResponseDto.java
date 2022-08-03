package com.server.insta.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.server.insta.config.Entity.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Data
@Builder
public class GetSearchUsersResponseDto {

    @Schema(description = "이름 검색")
    private String name;

    @Schema(description = "아이디 검색")
    private String username;

    @Schema(description = "회원 가입 일자")
    private String createdAt;

    @Schema(description = "회원 상태")
    @Enumerated(EnumType.STRING)
    private Status status;
}
