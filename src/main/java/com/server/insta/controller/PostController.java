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
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "게시물 저장", description = "게시물은 이미지, 동영상 둘 다 URL로 입력받는다. 최소 1개 이상의 이미지 or 동영상을 업로드 해야한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "SUCCESS", description = "응답 성공"),
            @ApiResponse(responseCode = "U001", description = "존재하지 않는 유저입니다."),
            @ApiResponse(responseCode = "DB", description = "데이터베이스 오류입니다."),
            @ApiResponse(responseCode = "SERVER",description = "서버와의 연결에 실패했습니다.")
    })
    @PostMapping("")
    public CommonResult createPost(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid CreatePostRequestDto dto

    ){
        if(dto.getMedias().isEmpty())   return responseService.getFailResult("VALID","이미지 or 동영상을 최소 1개이상 업로드 해야합니다.");
        else if(dto.getMedias().size()>10)  return responseService.getFailResult("VALID", "이미지 or 동영상은 최대 10개까지 업로드 가능합니다.");
        postService.createPost(userDetails.getUsername(), dto);
        return responseService.getSuccessResult();
    }

    @Operation(summary = "게시물 단건 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "SUCCESS", description = "응답 성공"),
            @ApiResponse(responseCode = "P001", description = "존재하지 않는 게시물입니다."),
            @ApiResponse(responseCode = "DB", description = "데이터베이스 오류입니다."),
            @ApiResponse(responseCode = "SERVER",description = "서버와의 연결에 실패했습니다.")
    })
    @GetMapping("/{postId}")
    public SingleResult<GetPostResponseDto> getPost(
            @PathVariable Long postId
    ){
        return responseService.getSingleResult(postService.getPost(postId));
    }

    @Operation(summary = "피드 조회", description = "한 페이지마다 10개의 피드가 구성됩니다."+
            " 처음 lastPostId를 null로 넘겨서 최근 10개의 게시물을 리턴받고" +
            " 마지막 게시물의 postId값(가장 작은 postId값)을 lastPostId에 넘겨주면 그 다음 페이지가 나오는 no offset 방식입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "SUCCESS", description = "응답 성공"),
            @ApiResponse(responseCode = "U001", description = "존재하지 않는 유저입니다."),
            @ApiResponse(responseCode = "DB", description = "데이터베이스 오류입니다."),
            @ApiResponse(responseCode = "SERVER",description = "서버와의 연결에 실패했습니다.")
    })
    @GetMapping("/feed")
    public MultipleResult<GetFeedResponseDto> getFeed(
            @AuthenticationPrincipal UserDetails userDetails,
            @Parameter(description = "처음엔 null, 이후엔 조회된 피드의 가장 작은 postId값을 넣어주세요.") @RequestParam (required = false) Long lastPostId,
            @Parameter(description = "페이징 피드 조회 페이지 사이즈, 기본값 10") @RequestParam(value = "size",defaultValue = "10") int pageSize
    ){
        return responseService.getMultipleResult(postService.getFeed(userDetails.getUsername(), lastPostId,pageSize));
    }

    @Operation(summary = "게시물 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "SUCCESS", description = "응답 성공"),
            @ApiResponse(responseCode = "U001", description = "존재하지 않는 유저입니다."),
            @ApiResponse(responseCode = "P001", description = "존재하지 않는 게시물입니다."),
            @ApiResponse(responseCode = "U005", description = "권한이 없는 유저입니다."),
            @ApiResponse(responseCode = "DB", description = "데이터베이스 오류입니다."),
            @ApiResponse(responseCode = "SERVER",description = "서버와의 연결에 실패했습니다.")
    })
    @PatchMapping("/{postId}/status")
    public CommonResult deletePost(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long postId
    ){
        postService.deletePost(userDetails.getUsername(),postId);
        return responseService.getSuccessResult();
    }

    @Operation(summary = "게시물 수정", description = "게시물 설명,이미지,태그 3가지 수정 가능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "SUCCESS", description = "응답 성공"),
            @ApiResponse(responseCode = "U001", description = "존재하지 않는 유저입니다."),
            @ApiResponse(responseCode = "P001", description = "존재하지 않는 게시물입니다."),
            @ApiResponse(responseCode = "U005", description = "권한이 없는 유저입니다."),
            @ApiResponse(responseCode = "DB", description = "데이터베이스 오류입니다."),
            @ApiResponse(responseCode = "SERVER",description = "서버와의 연결에 실패했습니다.")
    })
    @PatchMapping("/{postId}")
    public CommonResult updatePost(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long postId,
            @RequestBody @Valid UpdatePostRequestDto dto
            ){
        if(dto.getMedias().isEmpty())   return responseService.getFailResult("VALID","이미지 or 동영상을 최소 1개이상 업로드 해야합니다.");
        else if(dto.getMedias().size()>10)  return responseService.getFailResult("VALID", "이미지 or 동영상은 최대 10개까지 업로드 가능합니다.");
        postService.updatePost(userDetails.getUsername(), postId, dto);
        return responseService.getSuccessResult();
    }


}
