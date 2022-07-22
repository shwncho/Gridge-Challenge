package com.server.insta.controller;

import com.server.insta.config.response.ResponseService;
import com.server.insta.config.response.result.CommonResult;
import com.server.insta.config.response.result.MultipleResult;
import com.server.insta.dto.response.GetFollowerResponseDto;
import com.server.insta.dto.response.GetFollowingResponseDto;
import com.server.insta.service.FollowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name="follow", description = "Follow API")
@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;
    private final ResponseService responseService;

    @Operation(summary = "팔로우", description = "팔로우 하고싶은 유저의 id값을 넣어주면 된다.")
    @PostMapping("/{toUserid}")
    public CommonResult follow(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long toUserid
    ){
        followService.follow(userDetails.getUsername(), toUserid);
        return responseService.getSuccessResult();
    }

    @Operation(summary = "팔로우끊기", description = "팔로우를 끊고 싶은 유저의 id값을 넣어주면 된다.")
    @DeleteMapping("/{toUserid}")
    public CommonResult unFollow(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long toUserid
    ){
        followService.unFollow(userDetails.getUsername(), toUserid);
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
