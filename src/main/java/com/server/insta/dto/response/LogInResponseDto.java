package com.server.insta.dto.response;

import lombok.Data;

@Data
public class LogInResponseDto {

    private Long user_id;

    private String access_token;

    public LogInResponseDto(Long user_id, String access_token) {
        this.user_id = user_id;
        this.access_token = access_token;
    }
}
