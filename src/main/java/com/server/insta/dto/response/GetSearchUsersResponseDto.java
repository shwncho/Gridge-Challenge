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

    @Schema(description = "회원 번호(유저 db id)")
    private Long userId;

    @Schema(description = "회원 이름")
    private String name;

    @Schema(description = "회원 아이디")
    private String username;

    @Schema(description = "회원 가입 일자")
    private String createdAt;

    @Schema(description = "회원 상태")
    @Enumerated(EnumType.STRING)
    private Status status;
}
