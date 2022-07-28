package com.server.insta.controller;

import com.server.insta.config.response.ResponseService;
import com.server.insta.config.response.result.CommonResult;
import com.server.insta.config.response.result.MultipleResult;
import com.server.insta.config.response.result.SingleResult;
import com.server.insta.dto.request.CreateCommentRequestDto;
import com.server.insta.dto.response.GetCommentsResponseDto;
import com.server.insta.dto.response.PostMapToCommentsDto;
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

    @Operation(summary = "해당 게시글의 전체 댓글 조회", description = "한 페이지마다 10개의 댓글이 조회됩니다."+
            "처음 lastCommentId를 null로 넘겨서 최근 10개의 댓글을 리턴받고 " +
            "마지막 댓글의 commentId값(가장 작은 commentId값)을 lastCommentId에 넘겨주면 그 다음 페이지가 나오는 no offset 방식입니다.")
    @GetMapping("/{postId}")
    public SingleResult<PostMapToCommentsDto> getComments(
            @PathVariable Long postId,
            @RequestParam (required = false) Long lastCommentId,
            @RequestParam(value = "size",defaultValue = "10") int pageSize
    ){
        return responseService.getSingleResult(commentService.getComments(postId,lastCommentId,pageSize));
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
