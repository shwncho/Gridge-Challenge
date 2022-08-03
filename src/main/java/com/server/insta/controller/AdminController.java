package com.server.insta.controller;

import com.server.insta.config.Entity.Status;
import com.server.insta.config.response.ResponseService;
import com.server.insta.config.response.result.CommonResult;
import com.server.insta.config.response.result.MultipleResult;
import com.server.insta.dto.response.GetReportsResponseDto;
import com.server.insta.dto.response.GetSearchUsersResponseDto;
import com.server.insta.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@Api(tags="Admin API")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ResponseService responseService;
    private final AdminService adminService;


    @Operation(summary = "신고조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "SUCCESS", description = "응답 성공"),
            @ApiResponse(responseCode = "U001", description = "존재하지 않는 유저입니다."),
            @ApiResponse(responseCode = "U008", description = "관리자 계정이 아닙니다."),
            @ApiResponse(responseCode = "DB", description = "데이터베이스 오류입니다."),
            @ApiResponse(responseCode = "SERVER",description = "서버와의 연결에 실패했습니다.")
    })
    @GetMapping("/report")
    public MultipleResult<GetReportsResponseDto> getReports(
            @AuthenticationPrincipal UserDetails userDetails
    ){
        return responseService.getMultipleResult(adminService.getReports(userDetails.getUsername()));
    }

    @Operation(summary = "신고삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "SUCCESS", description = "응답 성공"),
            @ApiResponse(responseCode = "U001", description = "존재하지 않는 유저입니다."),
            @ApiResponse(responseCode = "U008", description = "관리자 계정이 아닙니다."),
            @ApiResponse(responseCode = "R003", description = "존재하지 않는 신고입니다."),
            @ApiResponse(responseCode = "DB", description = "데이터베이스 오류입니다."),
            @ApiResponse(responseCode = "SERVER",description = "서버와의 연결에 실패했습니다.")
    })
    @PatchMapping("/report/{reportId}")
    public CommonResult deleteReport(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long reportId
    ){
        adminService.deleteReport(userDetails.getUsername(), reportId);
        return responseService.getSuccessResult();
    }

    @Operation(summary = "신고된 게시물 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "SUCCESS", description = "응답 성공"),
            @ApiResponse(responseCode = "DB", description = "데이터베이스 오류입니다."),
            @ApiResponse(responseCode = "U001", description = "존재하지 않는 유저입니다."),
            @ApiResponse(responseCode = "U008", description = "관리자 계정이 아닙니다."),
            @ApiResponse(responseCode = "P001", description = "존재하지 않는 게시물입니다."),
            @ApiResponse(responseCode = "SERVER",description = "서버와의 연결에 실패했습니다.")
    })
    @PatchMapping("/report/post/delete/{postId}")
    public CommonResult deletePost(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long postId
    ){
        adminService.deletePost(userDetails.getUsername(), postId);
        return responseService.getSuccessResult();
    }

    @Operation(summary = "신고된 게시물 복원")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "SUCCESS", description = "응답 성공"),
            @ApiResponse(responseCode = "U001", description = "존재하지 않는 유저입니다."),
            @ApiResponse(responseCode = "U008", description = "관리자 계정이 아닙니다."),
            @ApiResponse(responseCode = "P001", description = "존재하지 않는 게시물입니다."),
            @ApiResponse(responseCode = "DB", description = "데이터베이스 오류입니다."),
            @ApiResponse(responseCode = "SERVER",description = "서버와의 연결에 실패했습니다.")
    })
    @PatchMapping("/report/post/restore/{postId}")
    public CommonResult restorePost(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long postId
    ){
        adminService.restorePost(userDetails.getUsername(), postId);
        return responseService.getSuccessResult();
    }

    @Operation(summary = "신고된 댓글 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "SUCCESS", description = "응답 성공"),
            @ApiResponse(responseCode = "U001", description = "존재하지 않는 유저입니다."),
            @ApiResponse(responseCode = "U008", description = "관리자 계정이 아닙니다."),
            @ApiResponse(responseCode = "C001", description = "존재하지 않는 댓글입니다."),
            @ApiResponse(responseCode = "DB", description = "데이터베이스 오류입니다."),
            @ApiResponse(responseCode = "SERVER",description = "서버와의 연결에 실패했습니다.")
    })
    @PatchMapping("/report/comment/delete/{commentId}")
    public CommonResult deleteComment(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long commentId
    ){
        adminService.deleteComment(userDetails.getUsername(), commentId);
        return responseService.getSuccessResult();
    }

    @Operation(summary = "신고된 댓글 복원")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "SUCCESS", description = "응답 성공"),
            @ApiResponse(responseCode = "U001", description = "존재하지 않는 유저입니다."),
            @ApiResponse(responseCode = "U008", description = "관리자 계정이 아닙니다."),
            @ApiResponse(responseCode = "C001", description = "존재하지 않는 댓글입니다."),
            @ApiResponse(responseCode = "DB", description = "데이터베이스 오류입니다."),
            @ApiResponse(responseCode = "SERVER",description = "서버와의 연결에 실패했습니다.")
    })
    @PatchMapping("/report/comment/restore/{commentId}")
    public CommonResult restoreComment(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long commentId
    ){
        adminService.restoreComment(userDetails.getUsername(), commentId);
        return responseService.getSuccessResult();
    }

    @Operation(summary = "회원조회", description = "날짜는 YYYY-MM-DD 이런 형식으로 보내주세요. "+
    "Status 예시: ACTIVE,INACTIVE, BLOCK, DELETED")
    @GetMapping("/users")
    public MultipleResult<GetSearchUsersResponseDto> getSearchUsers(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String joinedDate,
            @RequestParam(required = false) Status status
    ){
        return responseService.getMultipleResult(adminService.getSearchUsers(userDetails.getUsername(), name, username, joinedDate, status));
    }


}
