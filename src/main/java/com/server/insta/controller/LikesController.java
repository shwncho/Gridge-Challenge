package com.server.insta.controller;

import com.server.insta.config.response.ResponseService;
import com.server.insta.config.response.result.CommonResult;
import com.server.insta.config.response.result.MultipleResult;
import com.server.insta.dto.response.GetLikeUsersResponseDto;
import com.server.insta.service.LikesService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "좋아요")
    @PostMapping("/{postId}")
    public CommonResult actLike(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long postId
    ){

        likesService.actLike(userDetails.getUsername(), postId);
        return responseService.getSuccessResult();
    }


    @Operation(summary = "게시물에 좋아요한 유저들 조회")
    @GetMapping("/{postId}")
    public MultipleResult<GetLikeUsersResponseDto> getLikeUsers(
            @PathVariable Long postId
    ){

        return responseService.getMultipleResult(likesService.getLikeUsers(postId));
    }
}
