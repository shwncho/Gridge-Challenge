package com.server.insta.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SnsSignInResponseDto {

    @Schema(description = "oAuth 유저 이메일 정보")
    private String email;

    @Schema(description = "oAuth 유저 고유 id 정보")
    private String password;


}
