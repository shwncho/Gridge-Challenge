package com.server.insta.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.server.insta.config.Entity.Provider;
import com.server.insta.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
public class SignUpRequestDto {

    @NotBlank
    @Schema(description = "사용자 이름")
    @Pattern(regexp = "^[a-z0-9_.]{1,20}$",
            message = "사용자 이름은 소문자 영어, 숫자,'_', '.'만 20자 이내로 사용가능합니다.")
    private String username;

    @NotBlank
    @Schema(description = "유저 실명")
    @Size(max=20)
    private String name;

    @Schema(description = "oAuth email, 소셜로그인일경우 이메일을 넣어주고 일반 유저는 null로 보내주시면 됩니다.")
    private String email;


    @NotBlank
    @Schema(description = "유저 비밀번호")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Pattern(regexp = "^(?=.*[@$!%*#?&])[A-Za-z\\dㄱ-힣@$!%*#?&]{6,20}$",
            message = "비밀번호는 공백없이 특수문자가 적어도 1개 이상이 포함된 6자~20자의 비밀번호이어야 합니다.")
    private String password;


    @NotBlank
    @Schema(description = "유저 핸드폰 번호")
    @Pattern(regexp = "^\\d{11}$", message = "휴대폰 번호는 11자리 이어야 합니다.")
    private String phoneNumber;

    @NotNull
    @Schema(description = "유저 생년월일", example = "2000-01-01")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birth;

    @NotNull
    @Schema(description = "oAuth Provider", example = "KAKAO,NAVER,GOOGLE,FACEBOOK,NORMAL")
    private Provider provider;


    public User toEntity(){
        return User.builder()
                .email(email!=null ? email : null)
                .username(this.username)
                .name(this.name)
                .password(this.password)
                .phoneNumber(this.phoneNumber)
                .birth(this.birth)
                .provider(this.provider)
                .isPublic(true)
                .scheduler(LocalDateTime.now())
                .build();
    }
}
