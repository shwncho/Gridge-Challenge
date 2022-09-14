package com.server.insta.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.server.insta.config.Entity.Provider;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignInRequestDto {

    @Schema(description = "소셜 로그인 email")
    private String email;

    @Schema(description = "사용자 이름")
    private String username;

    @Schema(description = "유저 비밀번호")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Pattern(regexp = "^(?=.*[@$!%*#?&])[A-Za-z\\dㄱ-힣@$!%*#?&]{6,20}$",
            message = "비밀번호는 공백없이 특수문자가 적어도 1개 이상이 포함된 6자~20자의 비밀번호이어야 합니다.")
    private String password;


    public UsernamePasswordAuthenticationToken toAuthentication(){
        return new UsernamePasswordAuthenticationToken(username,password);
    }
}
