package com.server.insta.controller;

import com.server.insta.config.response.ResponseService;
import com.server.insta.config.response.result.CommonResult;
import com.server.insta.config.response.result.SingleResult;
import com.server.insta.dto.request.ResetPasswordRequestDto;
import com.server.insta.dto.request.UpdateProfileRequestDto;
import com.server.insta.dto.response.GetUserPageDto;
import com.server.insta.log.NoLogging;
import com.server.insta.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "SUCCESS", description = "응답 성공"),
            @ApiResponse(responseCode = "U001", description = "존재하지 않는 유저입니다."),
            @ApiResponse(responseCode = "DB", description = "데이터베이스 오류입니다."),
            @ApiResponse(responseCode = "SERVER",description = "서버와의 연결에 실패했습니다.")
    })
    @GetMapping("/{userId}")
    public SingleResult<GetUserPageDto> getUserPage(
            @PathVariable Long userId,
            @RequestParam(required = false) Long lastPostId,
            @RequestParam(value = "size",defaultValue = "9") int pageSize
    ){
        return responseService.getSingleResult(userService.getUserPage(userId,lastPostId,pageSize));
    }

    @Operation(summary = "비밀번호 재설정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "SUCCESS", description = "응답 성공"),
            @ApiResponse(responseCode = "VALID", description = "비밀번호는 공백없이 특수문자가 적어도 1개 이상이 포함된 6자~20자의 비밀번호이어야 합니다."),
            @ApiResponse(responseCode = "U001", description = "존재하지 않는 유저입니다."),
            @ApiResponse(responseCode = "U007", description = "소셜 로그인 유저는 비밀번호를 변경할 수 없습니다."),
            @ApiResponse(responseCode = "DB", description = "데이터베이스 오류입니다."),
            @ApiResponse(responseCode = "SERVER",description = "서버와의 연결에 실패했습니다.")
    })
    @PatchMapping("/reset-password")
    public CommonResult resetPassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid ResetPasswordRequestDto dto
    ){
        userService.resetPassword(userDetails.getUsername(), dto);
        return responseService.getSuccessResult();
    }

    @Operation(summary = "프로필 수정", description = "Username을 변경할경우 Spring security 내부에 저장된 Username값이 달라지기 때문에 바뀐 username으로 재로그인을 해야합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "SUCCESS", description = "응답 성공"),
            @ApiResponse(responseCode = "U001", description = "존재하지 않는 유저입니다."),
            @ApiResponse(responseCode = "U002", description = "이미 존재하는 사용자 이름입니다."),
            @ApiResponse(responseCode = "VALID", description =  "이름은 20자 이내만 가능합니다."),
            @ApiResponse(responseCode = "VALID", description = "사용자 이름은 소문자 영어, 숫자,'_', '.'만 20자 이내로 사용가능합니다."),
            @ApiResponse(responseCode = "DB", description = "데이터베이스 오류입니다."),
            @ApiResponse(responseCode = "SERVER",description = "서버와의 연결에 실패했습니다.")
    })
    @PatchMapping("/profile")
    public CommonResult updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid UpdateProfileRequestDto dto
    ){
        userService.updateProfile(userDetails.getUsername(), dto);
        return responseService.getSuccessResult();
    }

    @Operation(summary = "공개/비공개 계정 전환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "SUCCESS", description = "응답 성공"),
            @ApiResponse(responseCode = "U001", description = "존재하지 않는 유저입니다."),
            @ApiResponse(responseCode = "DB", description = "데이터베이스 오류입니다."),
            @ApiResponse(responseCode = "SERVER",description = "서버와의 연결에 실패했습니다.")
    })
    @PatchMapping("/status")
    public CommonResult updateStatus(
            @AuthenticationPrincipal UserDetails userDetails
    ){
        userService.updateStatus(userDetails.getUsername());
        return responseService.getSuccessResult();
    }

}




