package com.server.insta.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class SendMessageRequestDto {

    @NotBlank
    @Size(max=200, message = "메세지는 최대 200자까지 가능합니다.")
    private String content;
}
