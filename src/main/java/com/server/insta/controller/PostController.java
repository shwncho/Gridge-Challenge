package com.server.insta.controller;

import com.server.insta.config.response.ResponseService;
import com.server.insta.config.response.result.CommonResult;
import com.server.insta.config.response.result.SingleResult;
import com.server.insta.dto.request.PostRequestDto;
import com.server.insta.dto.request.UpdatePostRequestDto;
import com.server.insta.dto.response.PostResponseDto;
import com.server.insta.service.PostService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name="post", description = "Post API")
@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final ResponseService responseService;

    @PostMapping("")
    public CommonResult savePost(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid PostRequestDto dto

    ){
        postService.savePost(userDetails.getUsername(), dto);
        return responseService.getSuccessResult();
    }

    @GetMapping("/{postId}")
    public SingleResult<PostResponseDto> getPost(
            @PathVariable Long postId
    ){
        return responseService.getSingleResult(postService.getPost(postId));
    }

    @PatchMapping("/{postId}/status")
    public CommonResult deletePost(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long postId
    ){
        postService.deletePost(userDetails.getUsername(),postId);
        return responseService.getSuccessResult();
    }

    @PatchMapping("/{postId}")
    public CommonResult updatePost(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long postId,
            @RequestBody UpdatePostRequestDto dto
            ){
        postService.updatePost(userDetails.getUsername(), postId, dto);
        return responseService.getSuccessResult();
    }


}
