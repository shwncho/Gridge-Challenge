package com.server.insta.controller;

import com.server.insta.config.response.ResponseService;
import com.server.insta.config.response.result.CommonResult;
import com.server.insta.config.response.result.MultipleResult;
import com.server.insta.config.response.result.SingleResult;
import com.server.insta.dto.response.GetReportsResponseDto;
import com.server.insta.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Api(tags="Admin API")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ResponseService responseService;
    private final AdminService adminService;


    @Operation(summary = "신고조회")
    @GetMapping("/report")
    public MultipleResult<GetReportsResponseDto> getReports(
            @AuthenticationPrincipal UserDetails userDetails
    ){
        return responseService.getMultipleResult(adminService.getReports(userDetails.getUsername()));
    }

    @Operation(summary = "신고삭제")
    @DeleteMapping("/report/{reportId}")
    public CommonResult deleteReport(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long reportId
    ){
        adminService.deleteReport(userDetails.getUsername(), reportId);
        return responseService.getSuccessResult();
    }

    @Operation(summary = "신고된 게시물 삭제")
    @PatchMapping("/report/post/delete/{postId}")
    public CommonResult deletePost(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long postId
    ){
        adminService.deletePost(userDetails.getUsername(), postId);
        return responseService.getSuccessResult();
    }

    @Operation(summary = "신고된 게시물 복원")
    @PatchMapping("/report/post/restore/{postId}")
    public CommonResult restorePost(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long postId
    ){
        adminService.restorePost(userDetails.getUsername(), postId);
        return responseService.getSuccessResult();
    }

    @Operation(summary = "신고된 댓글 삭제")
    @PatchMapping("/report/comment/delete/{commentId}")
    public CommonResult deleteComment(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long commentId
    ){
        adminService.deleteComment(userDetails.getUsername(), commentId);
        return responseService.getSuccessResult();
    }

    @Operation(summary = "신고된 댓글 복원")
    @PatchMapping("/report/comment/restore/{commentId}")
    public CommonResult restoreComment(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long commentId
    ){
        adminService.restoreComment(userDetails.getUsername(), commentId);
        return responseService.getSuccessResult();
    }


}
