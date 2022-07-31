package com.server.insta.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Data
public class AdminStatusRequestDto {

    @Schema(description = "관리자 계정으로 변경할 계정의 아이디를 넣어주세요.")
    private String adminId;

    @Schema(description = "관리자 계정으로 변경할 계정의 비밀번호를 넣어주세요.")
    private String password;

    public UsernamePasswordAuthenticationToken toAuthentication(){
        return new UsernamePasswordAuthenticationToken(adminId,password);
    }

}
