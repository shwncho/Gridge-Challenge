package com.server.insta.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ReportPostRequestDto {

    @NotBlank
    private String reason;
}
