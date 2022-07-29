package com.server.insta.controller;

import com.server.insta.config.response.ResponseService;
import com.server.insta.config.response.result.CommonResult;
import com.server.insta.config.response.result.SingleResult;
import com.server.insta.dto.request.ResetPasswordRequestDto;
import com.server.insta.dto.request.UpdateProfileRequestDto;
import com.server.insta.dto.response.GetUserPageDto;
import com.server.insta.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags="User API")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final ResponseService responseService;
    private final UserService userService;

    @Operation(summary = "유저 페이지 조회", description = "한 페이지마다 9개의 피드가 구성됩니다."+
            " 처음 lastPostId를 null로 넘겨서 최근 9개의 게시물을 리턴받고" +
            " 마지막 게시물의 postId값(가장 작은 postId값)을 lastPostId에 넘겨주면 그 다음 페이지가 나오는 no offset 방식입니다.")
    @GetMapping("/{userId}")
    public SingleResult<GetUserPageDto> getUserPage(
            @PathVariable Long userId,
            @RequestParam(required = false) Long lastPostId,
            @RequestParam(value = "size",defaultValue = "9") int pageSize
    ){
        return responseService.getSingleResult(userService.getUserPage(userId,lastPostId,pageSize));
    }

    @Operation(summary = "비밀번호 재설정")
    @PatchMapping("/reset-password")
    public CommonResult resetPassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid ResetPasswordRequestDto dto
    ){
        userService.resetPassword(userDetails.getUsername(), dto);
        return responseService.getSuccessResult();
    }

    @Operation(summary = "프로필 수정")
    @PatchMapping("/profile")
    public CommonResult updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid UpdateProfileRequestDto dto
    ){
        userService.updateProfile(userDetails.getUsername(), dto);
        return responseService.getSuccessResult();
    }

}




