package com.server.insta.controller;

import com.server.insta.config.response.ResponseService;
import com.server.insta.config.response.result.CommonResult;
import com.server.insta.config.response.result.MultipleResult;
import com.server.insta.config.response.result.SingleResult;
import com.server.insta.dto.request.CreatePostRequestDto;
import com.server.insta.dto.request.UpdatePostRequestDto;
import com.server.insta.dto.response.GetPostResponseDto;
import com.server.insta.dto.response.GetFeedResponseDto;
import com.server.insta.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags="Post API")
@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final ResponseService responseService;

    @Operation(summary = "게시물 저장")
    @PostMapping("")
    public CommonResult createPost(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid CreatePostRequestDto dto

    ){
        postService.createPost(userDetails.getUsername(), dto);
        return responseService.getSuccessResult();
    }

    @Operation(summary = "게시물 단건 조회")
    @GetMapping("/{postId}")
    public SingleResult<GetPostResponseDto> getPost(
            @PathVariable Long postId
    ){
        return responseService.getSingleResult(postService.getPost(postId));
    }

    @Operation(summary = "피드 조회", description = "한 페이지마다 10개의 피드가 구성됩니다."+
    " 처음 10개의 피드를 리턴받고, 마지막 게시물의 postId값을 lastPostId에 넘겨주면 그 다음 페이지가 나오는 no offset 방식입니다.")
    @GetMapping("/feed")
    public MultipleResult<GetFeedResponseDto> getFeed(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam (required = false) Long lastPostId,
            @RequestParam(value = "size",defaultValue = "10") int pageSize
    ){
        return responseService.getMultipleResult(postService.getFeed(userDetails.getUsername(), lastPostId,pageSize));
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
