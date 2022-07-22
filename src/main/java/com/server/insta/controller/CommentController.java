package com.server.insta.controller;

import com.server.insta.config.response.ResponseService;
import com.server.insta.config.response.result.CommonResult;
import com.server.insta.config.response.result.MultipleResult;
import com.server.insta.dto.request.CreateCommentRequestDto;
import com.server.insta.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name="comment", description = "Comment API")
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
        commentService.createPost(userDetails.getUsername(), postId, dto);
        return responseService.getSuccessResult();
    }

}
