package com.server.insta.dto.request;

import com.server.insta.config.Entity.Provider;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SnsSignInRequestDto {

    @NotBlank
    private String token;

    @NotBlank
    @Schema(description = "oAuth Provider", example = "KAKAO,NAVER,GOOGLE,FACEBOOK,NORMAL")
    private Provider provider;
}
