package com.server.insta.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.server.insta.domain.User;
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

    @Schema(description = "유저 실명")
    private String username;

    @Schema(description = "유저 닉네임")
    private String nickname;
    
    @Schema(description = "유저 비밀번호")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    

    public User toEntity(){
        return User.builder()
                .email(this.email)
                .username(this.username)
                .password(this.password)
                .nickname(this.nickname)
                .build();
    }
}
