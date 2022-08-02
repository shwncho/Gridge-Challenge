package com.server.insta.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


@Data
public class SignInRequestDto {

    @Schema(description = "소셜 로그인 email")
    private String email;

    @Schema(description = "사용자 이름")
    private String username;

    @Schema(description = "유저 비밀번호")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    public UsernamePasswordAuthenticationToken toAuthentication(){
        return new UsernamePasswordAuthenticationToken(username,password);
    }
}
