package com.server.insta.controller;

import com.server.insta.config.response.ResponseService;
import com.server.insta.config.response.result.CommonResult;
import com.server.insta.config.response.result.MultipleResult;
import com.server.insta.dto.response.GetFollowerResponseDto;
import com.server.insta.dto.response.GetFollowingResponseDto;
import com.server.insta.service.FollowService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
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
    @PostMapping("/{toUserId}")
    public CommonResult actFollow(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long toUserId
    ){
        followService.actFollow(userDetails.getUsername(), toUserId);
        return responseService.getSuccessResult();
    }

    @Operation(summary = "팔로잉 유저들 조회")
    @GetMapping("/following/{id}")
    public MultipleResult<GetFollowingResponseDto> getFollowing(
            @PathVariable Long id
    ){
        return responseService.getMultipleResult(followService.getFollowing(id));
    }

    @Operation(summary = "팔로워들 조회")
    @GetMapping("/follower/{id}")
    public MultipleResult<GetFollowerResponseDto> getFollower(
            @PathVariable Long id
    ){
        return responseService.getMultipleResult(followService.getFollower(id));
    }

}
