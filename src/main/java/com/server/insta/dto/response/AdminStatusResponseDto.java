package com.server.insta.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AdminStatusResponseDto {

    @Schema(description = "관리자 jwt 토큰(access_token)")
    private String access_token;


    public AdminStatusResponseDto(String access_token) {
        this.access_token = access_token;
    }
}
