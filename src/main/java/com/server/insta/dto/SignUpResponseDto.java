package com.server.insta.dto;

import com.server.insta.entity.User;
import lombok.Data;

@Data
public class SignUpResponseDto {

    private Long user_id;

    private String access_token;

    public SignUpResponseDto(User user){
        this.user_id = user.getId();
    }


}
