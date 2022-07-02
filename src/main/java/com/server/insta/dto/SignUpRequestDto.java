package com.server.insta.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.server.insta.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.Email;

@Data
@NoArgsConstructor
public class SignUpRequestDto {

    @Email
    @Schema(description = "유저 이메일")
    private String email;

    @Schema(description = "유저 비밀번호")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Schema(description = "유저 닉네임")
    private String nickName;

    @Schema(description = "유저 휴대폰 번호")
    private String phoneNumber;

    @Schema(description = "유저 프로필 사진")
    private String profileImgUrl;

    public UsernamePasswordAuthenticationToken toAuthentication(){
        return new UsernamePasswordAuthenticationToken(email,password);
    }

    // parameter 없으므로 @NoArgsConstructor 생성
    @Builder
    public User toEntity(){
        return User.builder()
                .email(this.email)
                .password(this.password)
                .nickName(this.nickName)
                .phoneNumber(this.phoneNumber)
                .profileImgUrl(this.profileImgUrl)
                .build();
    }
}
