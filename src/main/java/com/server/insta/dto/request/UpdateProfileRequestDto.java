package com.server.insta.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UpdateProfileRequestDto {

    @Schema(description = "프로필 사진")
    private String profileImgUrl;

    @NotBlank
    @Schema(description = "유저 실명")
    @Size(max=20, message = "이름은 20자 이내만 가능합니다.")
    private String name;

    @NotBlank
    @Schema(description = "사용자 이름")
    @Pattern(regexp = "^[a-z0-9_.]{1,20}$",
            message = "사용자 이름은 소문자 영어, 숫자,'_', '.'만 20자 이내로 사용가능합니다.")
    private String username;

    @Schema(description = "웹사이트")
    private String website;

    @Schema(description = "유저 소개")
    private String introduce;
}
