package com.server.insta.dto.response;

import com.server.insta.config.Entity.Provider;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SnsSignInResponseDto {

    @Schema(description = "유저 db id")
    private Long user_id;

    @Schema(description = "유저 jwt 토큰(access_token)")
    private String access_token;

    //카카오 관련 필드
    @Schema(description = "카카오 이메일")
    private String email;

    //카카오 id
    @Schema(description = "카카오 고유 id")
    private Long id;

    @Schema(description = "oAuth Provider")
    private Provider provider;

    public static SnsSignInResponseDto createKaKaoProfile(String email, Long id){
        return SnsSignInResponseDto.builder()
                .email(email)
                .id(id)
                .build();
    }

    public static SnsSignInResponseDto createKaKaoUser(Long userId, String token, Provider provider){
        return SnsSignInResponseDto.builder()
                .user_id(userId)
                .access_token(token)
                .provider(provider)
                .build();
    }


}
