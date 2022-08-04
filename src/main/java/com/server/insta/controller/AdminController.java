package com.server.insta.controller;

import com.server.insta.config.Entity.Status;
import com.server.insta.config.response.ResponseService;
import com.server.insta.config.response.result.CommonResult;
import com.server.insta.config.response.result.MultipleResult;
import com.server.insta.config.response.result.SingleResult;
import com.server.insta.dto.response.GetReportsResponseDto;
import com.server.insta.dto.response.GetSearchPostsResponseDto;
import com.server.insta.dto.response.GetSearchUsersResponseDto;
import com.server.insta.dto.response.GetUserInfoResponseDto;
import com.server.insta.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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


    @Operation(summary = "신고조회", description = "신고는 페이징을 통해 조회 가능합니다. 페이지 인덱스는 0부터 시작합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "SUCCESS", description = "응답 성공"),
            @ApiResponse(responseCode = "U001", description = "존재하지 않는 유저입니다."),
            @ApiResponse(responseCode = "U008", description = "관리자 계정이 아닙니다."),
            @ApiResponse(responseCode = "DB", description = "데이터베이스 오류입니다."),
            @ApiResponse(responseCode = "SERVER",description = "서버와의 연결에 실패했습니다.")
    })
    @GetMapping("/report")
    public MultipleResult<GetReportsResponseDto> getReports(
            @AuthenticationPrincipal UserDetails userDetails,
            @Parameter(description = "페이징 회원 조회 인덱스, 0부터 시작합니다") @RequestParam int pageIndex,
            @Parameter(description = "페이징 회원 조회 사이즈") @RequestParam(value = "size") int pageSize
    ){
        return responseService.getMultipleResult(adminService.getReports(userDetails.getUsername(),pageIndex, pageSize));
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

    @Operation(summary = "회원조회", description = " 회원은 페이징을 통해 조회 가능합니다. 페이지 인덱스는 0부터 시작합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "SUCCESS", description = "응답 성공"),
            @ApiResponse(responseCode = "U001", description = "존재하지 않는 유저입니다."),
            @ApiResponse(responseCode = "U008", description = "관리자 계정이 아닙니다."),
            @ApiResponse(responseCode = "ADM001", description = "날짜 형식이 올바르지 않습니다."),
            @ApiResponse(responseCode = "DB", description = "데이터베이스 오류입니다."),
            @ApiResponse(responseCode = "SERVER", description = "서버와의 연결에 실패했습니다.")
    })
    @GetMapping("/users")
    public MultipleResult<GetSearchUsersResponseDto> getSearchUsers(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String username,
            @Parameter(description = "날짜는 YYYY-MM-DD 이런 형식으로 보내주세요.") @RequestParam(required = false) String joinedDate,
            @RequestParam(required = false) Status status,
            @Parameter(description = "페이징 회원 조회 인덱스, 0부터 시작합니다") @RequestParam int pageIndex,
            @Parameter(description = "페이징 회원 조회 사이즈") @RequestParam(value = "size") int pageSize
    ){
        return responseService.getMultipleResult(adminService.getSearchUsers(userDetails.getUsername(), name, username, joinedDate, status,pageIndex,pageSize));
    }

    @Operation(summary = "회원 상세 조회")
    @GetMapping("/user/{userId}")
    public SingleResult<GetUserInfoResponseDto> getUserInfo(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long userId
    ){
        return responseService.getSingleResult(adminService.getUserInfo(userDetails.getUsername(),userId));
    }

    @Operation(summary = "회원 정지")
    @PatchMapping("/user/block/{userId}")
    public CommonResult blockStatus(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long userId
    ){
        adminService.blockStatus(userDetails.getUsername(), userId);
        return responseService.getSuccessResult();
    }


    @Operation(summary = "피드 조회", description = " 피드는 페이징을 통해 조회 가능합니다. 페이지 인덱스는 0부터 시작합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "SUCCESS", description = "응답 성공"),
            @ApiResponse(responseCode = "U001", description = "존재하지 않는 유저입니다."),
            @ApiResponse(responseCode = "U008", description = "관리자 계정이 아닙니다."),
            @ApiResponse(responseCode = "ADM001", description = "날짜 형식이 올바르지 않습니다."),
            @ApiResponse(responseCode = "DB", description = "데이터베이스 오류입니다."),
            @ApiResponse(responseCode = "SERVER", description = "서버와의 연결에 실패했습니다.")
    })
    @GetMapping("/posts")
    public MultipleResult<GetSearchPostsResponseDto> getSearchPosts(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String username,
            @Parameter(description = "날짜는 YYYY-MM-DD 이런 형식으로 보내주세요.") @RequestParam(required = false) String createdDate,
            @RequestParam(required = false) Status status,
            @Parameter(description = "페이징 피드 조회 인덱스, 0부터 시작합니다") @RequestParam int pageIndex,
            @Parameter(description = "페이징 피드 조회 사이즈") @RequestParam(value = "size") int pageSize
    ){
        return responseService.getMultipleResult(adminService.getSearchPosts(userDetails.getUsername(),username,createdDate,status,pageIndex,pageSize));
    }
}
