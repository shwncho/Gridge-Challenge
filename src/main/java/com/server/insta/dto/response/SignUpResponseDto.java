package com.server.insta.dto.response;

import com.server.insta.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SignUpResponseDto {

    @Schema(description = "회원가입에 성공한 유저 db id")
    private Long user_id;


    public SignUpResponseDto(User user){
        this.user_id = user.getId();
    }


}
