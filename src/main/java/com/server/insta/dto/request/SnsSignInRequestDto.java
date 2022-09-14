package com.server.insta.dto.request;

import com.server.insta.config.Entity.Provider;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SnsSignInRequestDto {

    @NotBlank
    private String token;

    @NotNull
    @Schema(description = "oAuth Provider", example = "KAKAO,NAVER,GOOGLE,FACEBOOK,NORMAL")
    private Provider provider;
}
