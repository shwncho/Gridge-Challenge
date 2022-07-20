package com.server.insta.controller;

import com.server.insta.config.response.ResponseService;
import com.server.insta.config.response.result.CommonResult;
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

    @Operation(summary = "팔로우")
    @PostMapping("/{toUserid}")
    public CommonResult follow(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long toUserid
    ){
        followService.follow(userDetails.getUsername(), toUserid);
        return responseService.getSuccessResult();
    }

    @Operation(summary = "팔로우끊기")
    @DeleteMapping("/{toUserid}")
    public CommonResult unFollow(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long toUserid
    ){
        followService.unFollow(userDetails.getUsername(), toUserid);
        return responseService.getSuccessResult();
    }

}
