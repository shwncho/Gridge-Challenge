package com.server.insta.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.server.insta.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@NoArgsConstructor
public class SignUpRequestDto {

    @NotBlank
    @Schema(description = "유저 계정 아이디(전화번호,사용자 이름, 이메일)")
    @Size(min=3, max=20, message = "아이디는 3자리 이상 20자리 이하의 길이입니다.")
    private String email;

    @NotBlank
    @Schema(description = "유저 실명")
    private String username;

    @NotBlank
    @Schema(description = "유저 닉네임")
    private String nickname;

    @NotBlank
    @Schema(description = "유저 비밀번호")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Pattern(regexp = "(?=.*\\W)(?=\\S+$).{6,20}\"",
            message = "비밀번호는 특수문자가 적어도 1개 이상이 포함된 6자~20자의 비밀번호이어야 합니다.")
    private String password;


    @NotBlank
    @Schema(description = "유저 핸드폰 번호")
    @Pattern(regexp = "^\\d{11}$", message = "휴대폰 번호는 11자리 이어야 합니다.")
    private String phoneNumber;

    @NotBlank
    @Schema(description = "유저 생년월일")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birth;
    

    public User toEntity(){
        return User.builder()
                .email(this.email)
                .username(this.username)
                .password(this.password)
                .nickname(this.nickname)
                .phoneNumber(this.phoneNumber)
                .birth(this.birth)
                .build();
    }
}
