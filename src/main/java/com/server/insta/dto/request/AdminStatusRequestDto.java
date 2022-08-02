package com.server.insta.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.Pattern;

@Data
public class AdminStatusRequestDto {

    @Schema(description = "관리자 계정으로 변경할 계정의 아이디를 넣어주세요.")
    private String adminId;

    @Schema(description = "관리자 계정으로 변경할 계정의 비밀번호를 넣어주세요.")
    @Pattern(regexp = "^(?=.*[@$!%*#?&])[A-Za-z\\dㄱ-힣@$!%*#?&]{6,20}$",
            message = "비밀번호는 공백없이 특수문자가 적어도 1개 이상이 포함된 6자~20자의 비밀번호이어야 합니다.")
    private String password;

    public UsernamePasswordAuthenticationToken toAuthentication(){
        return new UsernamePasswordAuthenticationToken(adminId,password);
    }

}
