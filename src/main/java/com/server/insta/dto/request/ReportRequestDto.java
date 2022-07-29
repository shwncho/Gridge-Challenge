package com.server.insta.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ReportRequestDto {

    @NotBlank
    @Schema(description = "신고사유")
    private String reason;
}
