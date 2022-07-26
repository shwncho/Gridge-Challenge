package com.server.insta.controller;

import com.server.insta.config.response.ResponseService;
import com.server.insta.config.response.result.CommonResult;
import com.server.insta.config.response.result.MultipleResult;
import com.server.insta.dto.request.CreateCommentRequestDto;
import com.server.insta.dto.response.GetCommentsResponseDto;
import com.server.insta.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags="Comment API")
@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final ResponseService responseService;
    private final CommentService commentService;

    @Operation(summary = "댓글 작성")
    @PostMapping("/{postId}")
    public CommonResult createComment(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long postId,
            @RequestBody @Valid CreateCommentRequestDto dto
            ){
        commentService.createComment(userDetails.getUsername(), postId, dto);
        return responseService.getSuccessResult();
    }

    @Operation(summary = "해당 게시글의 전체 댓글 조회")
    @GetMapping("/{postId}")
    public MultipleResult<GetCommentsResponseDto> getComments(
            @PathVariable Long postId
    ){
        return responseService.getMultipleResult(commentService.getComments(postId));
    }

    @Operation(summary = "댓글 삭제")
    @PatchMapping("/{commentId}")
    public CommonResult deleteComment(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long commentId
    ){
        commentService.deletePost(userDetails.getUsername(), commentId);
        return responseService.getSuccessResult();
    }
}
