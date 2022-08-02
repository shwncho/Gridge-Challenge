package com.server.insta.controller;

import com.server.insta.config.response.ResponseService;
import com.server.insta.config.response.result.CommonResult;
import com.server.insta.config.response.result.MultipleResult;
import com.server.insta.dto.response.GetFollowerResponseDto;
import com.server.insta.dto.response.GetFollowingResponseDto;
import com.server.insta.service.FollowService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Api(tags="Follow API")
@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;
    private final ResponseService responseService;


    @Operation(summary = "팔로우/팔로우 취소")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "SUCCESS", description = "응답 성공"),
            @ApiResponse(responseCode = "DB", description = "데이터베이스 오류입니다."),
            @ApiResponse(responseCode = "SERVER",description = "서버와의 연결에 실패했습니다.")
    })
    @PostMapping("/{toUserId}")
    public CommonResult actFollow(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long toUserId
    ){
        followService.actFollow(userDetails.getUsername(), toUserId);
        return responseService.getSuccessResult();
    }

    @Operation(summary = "팔로우 승인", description = "비공개 계정이 팔로우 요청을 승인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "SUCCESS", description = "응답 성공"),
            @ApiResponse(responseCode = "DB", description = "데이터베이스 오류입니다."),
            @ApiResponse(responseCode = "SERVER",description = "서버와의 연결에 실패했습니다.")
    })
    @PostMapping("/approval/{fromUserId}")
    public CommonResult approveFollow(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long fromUserId
    ){
        followService.approveFollow(userDetails.getUsername(), fromUserId);
        return responseService.getSuccessResult();
    }

    @Operation(summary = "팔로우 거부", description = "비공개 계정이 팔로우 요청을 거절")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "SUCCESS", description = "응답 성공"),
            @ApiResponse(responseCode = "DB", description = "데이터베이스 오류입니다."),
            @ApiResponse(responseCode = "SERVER",description = "서버와의 연결에 실패했습니다.")
    })
    @PostMapping("/denial/{fromUserId}")
    public CommonResult denyFollow(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long fromUserId
    ){
        followService.denyFollow(userDetails.getUsername(), fromUserId);
        return responseService.getSuccessResult();
    }

    @Operation(summary = "팔로잉 유저들 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "SUCCESS", description = "응답 성공"),
            @ApiResponse(responseCode = "DB", description = "데이터베이스 오류입니다."),
            @ApiResponse(responseCode = "SERVER",description = "서버와의 연결에 실패했습니다.")
    })
    @GetMapping("/following/{id}")
    public MultipleResult<GetFollowingResponseDto> getFollowing(
            @PathVariable Long id
    ){
        return responseService.getMultipleResult(followService.getFollowing(id));
    }

    @Operation(summary = "팔로워들 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "SUCCESS", description = "응답 성공"),
            @ApiResponse(responseCode = "DB", description = "데이터베이스 오류입니다."),
            @ApiResponse(responseCode = "SERVER",description = "서버와의 연결에 실패했습니다.")
    })
    @GetMapping("/follower/{id}")
    public MultipleResult<GetFollowerResponseDto> getFollower(
            @PathVariable Long id
    ){
        return responseService.getMultipleResult(followService.getFollower(id));
    }

}
