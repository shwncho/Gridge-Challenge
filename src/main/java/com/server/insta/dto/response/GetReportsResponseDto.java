package com.server.insta.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
public class GetReportsResponseDto {

    @Schema(description = "신고된 게시물 db id")
    private Long postId;

    @Schema(description = "신고된 댓글 db id, null일 경우 게시물 신고, null이 아닐경우 댓글 신고")
    private Long commentId;

    @Schema(description = "신고사유")
    private String reason;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
