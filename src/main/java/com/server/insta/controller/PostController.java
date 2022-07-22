package com.server.insta.controller;

import com.server.insta.config.response.ResponseService;
import com.server.insta.config.response.result.CommonResult;
import com.server.insta.config.response.result.SingleResult;
import com.server.insta.dto.request.SavePostRequestDto;
import com.server.insta.dto.request.UpdatePostRequestDto;
import com.server.insta.dto.response.GetPostResponseDto;
import com.server.insta.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "게시물 저장")
    @PostMapping("")
    public CommonResult savePost(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid SavePostRequestDto dto

    ){
        postService.savePost(userDetails.getUsername(), dto);
        return responseService.getSuccessResult();
    }

    @Operation(summary = "게시물 조회")
    @GetMapping("/{postId}")
    public SingleResult<GetPostResponseDto> getPost(
            @PathVariable Long postId
    ){
        return responseService.getSingleResult(postService.getPost(postId));
    }

    @Operation(summary = "게시물 삭제")
    @PatchMapping("/{postId}/status")
    public CommonResult deletePost(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long postId
    ){
        postService.deletePost(userDetails.getUsername(),postId);
        return responseService.getSuccessResult();
    }

    @Operation(summary = "게시물 수정", description = "게시물 설명,이미지,태그 3가지 수정 가능")
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
