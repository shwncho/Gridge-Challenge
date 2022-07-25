package com.server.insta.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SignInResponseDto {

    @Schema(description = "유저 db id")
    private Long user_id;

    @Schema(description = "유저 jwt 토큰(access_token)")
    private String access_token;

    public SignInResponseDto(Long user_id, String access_token) {
        this.user_id = user_id;
        this.access_token = access_token;
    }
}
