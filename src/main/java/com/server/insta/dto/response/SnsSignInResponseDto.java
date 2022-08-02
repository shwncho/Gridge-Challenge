package com.server.insta.dto.response;

import com.server.insta.config.Entity.Provider;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SnsSignInResponseDto {

    @Schema(description = "oAuth email")
    private String email;

    @Schema(description = "oAuth 유저 고유 id 정보")
    private String password;

    @Schema(description = "oAuth provider")
    private Provider provider;


}
