package com.server.insta.controller;

import com.server.insta.config.response.ResponseService;
import com.server.insta.config.response.result.CommonResult;
import com.server.insta.config.response.result.MultipleResult;
import com.server.insta.dto.response.GetLikeUsersResponseDto;
import com.server.insta.service.LikesService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Api(tags="Like API")
@RestController
@RequestMapping("/api/like")
@RequiredArgsConstructor
public class LikesController {

    private final ResponseService responseService;
    private final LikesService likesService;

    @Operation(summary = "게시물 좋아요/좋아요 취소")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "SUCCESS", description = "응답 성공"),
            @ApiResponse(responseCode = "U001", description = "존재하지 않는 유저입니다."),
            @ApiResponse(responseCode = "P001", description = "존재하지 않는 게시물입니다."),
            @ApiResponse(responseCode = "DB", description = "데이터베이스 오류입니다."),
            @ApiResponse(responseCode = "SERVER",description = "서버와의 연결에 실패했습니다.")
    })
    @PostMapping("/post/{postId}")
    public CommonResult actLike(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long postId
    ){

        likesService.actLike(userDetails.getUsername(), postId);
        return responseService.getSuccessResult();
    }

    @Operation(summary = "댓글 좋아요/좋아요 취소")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "SUCCESS", description = "응답 성공"),
            @ApiResponse(responseCode = "U001", description = "존재하지 않는 유저입니다."),
            @ApiResponse(responseCode = "P001", description = "존재하지 않는 게시물입니다."),
            @ApiResponse(responseCode = "DB", description = "데이터베이스 오류입니다."),
            @ApiResponse(responseCode = "SERVER",description = "서버와의 연결에 실패했습니다.")
    })
    @PostMapping("/comment/{commentId}")
    public CommonResult actLikeToComment(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long commentId
    ){

        likesService.actLikeToComment(userDetails.getUsername(), commentId);
        return responseService.getSuccessResult();
    }


    @Operation(summary = "게시물에 좋아요한 유저들 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "SUCCESS", description = "응답 성공"),
            @ApiResponse(responseCode = "P001", description = "존재하지 않는 게시물입니다."),
            @ApiResponse(responseCode = "DB", description = "데이터베이스 오류입니다."),
            @ApiResponse(responseCode = "SERVER",description = "서버와의 연결에 실패했습니다.")
    })
    @GetMapping("/{postId}")
    public MultipleResult<GetLikeUsersResponseDto> getLikeUsers(
            @PathVariable Long postId
    ){

        return responseService.getMultipleResult(likesService.getLikeUsers(postId));
    }
}
