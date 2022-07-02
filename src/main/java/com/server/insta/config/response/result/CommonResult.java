package com.server.insta.config.response.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResult {

    @Schema(description= "응답 성공 여부: T/F")
    private boolean success;

    @Schema(description = "응답 메시지")
    private String message;
}
