package com.server.insta.controller;

import com.server.insta.config.response.ResponseService;
import com.server.insta.config.response.result.CommonResult;
import com.server.insta.dto.request.ReportRequestDto;
import com.server.insta.service.ReportService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags="Report API")
@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {

    private final ResponseService responseService;
    private final ReportService reportService;

    @Operation(summary = "게시물 신고",description ="신고횟수 10회 누적 될경우"+
    "신고횟수가 10회 누적되었으므로 게시글을 비공개합니다. 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "SUCCESS", description = "응답 성공"),
            @ApiResponse(responseCode = "U001", description = "존재하지 않는 유저입니다."),
            @ApiResponse(responseCode = "P001", description = "존재하지 않는 게시물입니다."),
            @ApiResponse(responseCode = "R001", description = "본인 게시글은 신고할 수 없습니다."),
            @ApiResponse(responseCode = "DB", description = "데이터베이스 오류입니다."),
            @ApiResponse(responseCode = "SERVER",description = "서버와의 연결에 실패했습니다.")
    })
    @PostMapping("/post/{postId}")
    public CommonResult reportPost(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long postId,
            @RequestBody @Valid ReportRequestDto dto
    ){
        int reportCount = reportService.reportPost(userDetails.getUsername(), postId, dto);
        if(reportCount==10) return responseService.getSuccessResult("신고횟수가 10회 누적되었으므로 게시글을 비공개합니다.");
        return responseService.getSuccessResult();
    }

    @Operation(summary = "댓글 신고",description ="신고횟수 10회 누적 될경우"+
            "신고횟수가 10회 누적되었으므로 댓글을 비공개합니다. 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "SUCCESS", description = "응답 성공"),
            @ApiResponse(responseCode = "U001", description = "존재하지 않는 유저입니다."),
            @ApiResponse(responseCode = "C001", description = "존재하지 않는 댓글입니다."),
            @ApiResponse(responseCode = "R001", description = "본인 게시글은 신고할 수 없습니다."),
            @ApiResponse(responseCode = "DB", description = "데이터베이스 오류입니다."),
            @ApiResponse(responseCode = "SERVER",description = "서버와의 연결에 실패했습니다.")
    })
    @PostMapping("/comment/{commentId}")
    public CommonResult reportComment(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long commentId,
            @RequestBody @Valid ReportRequestDto dto
    ){
        int reportCount = reportService.reportComment(userDetails.getUsername(), commentId, dto);
        if(reportCount==10) return responseService.getSuccessResult("신고횟수가 10회 누적되었으므로 댓글을 비공개합니다.");
        return responseService.getSuccessResult();
    }
}
