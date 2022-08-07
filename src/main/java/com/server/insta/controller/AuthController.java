package com.server.insta.controller;

import com.server.insta.config.response.ResponseService;
import com.server.insta.config.response.result.CommonResult;
import com.server.insta.config.response.result.SingleResult;
import com.server.insta.dto.request.AdminStatusRequestDto;
import com.server.insta.dto.request.SignInRequestDto;
import com.server.insta.dto.request.SnsSignInRequestDto;
import com.server.insta.dto.response.AdminStatusResponseDto;
import com.server.insta.dto.response.SignInResponseDto;
import com.server.insta.dto.request.SignUpRequestDto;
import com.server.insta.dto.response.SignUpResponseDto;
import com.server.insta.dto.response.SnsSignInResponseDto;
import com.server.insta.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags="Auth API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final ResponseService responseService;

    @Operation(summary="회원가입")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "SUCCESS", description = "응답 성공"),
            @ApiResponse(responseCode = "DB", description = "데이터베이스 오류입니다."),
            @ApiResponse(responseCode = "U002", description = "이미 존재하는 사용자 이름입니다."),
            @ApiResponse(responseCode = "SERVER",description = "서버와의 연결에 실패했습니다.")
    })
    @PostMapping("/signup")
    public SingleResult<SignUpResponseDto> signUp(
            @RequestBody @Valid SignUpRequestDto dto){
        return responseService.getSingleResult(authService.signUp(dto));
    }

    @Operation(summary="로그인",description = "소셜 로그인 일경우 email과 password를, 일반 로그인 일경우 username과 password를 넘겨주세요.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "SUCCESS", description = "응답 성공"),
            @ApiResponse(responseCode = "U001", description = "존재하지 않는 유저입니다."),
            @ApiResponse(responseCode = "U003", description = "비밀번호가 일치하지 않습니다."),
            @ApiResponse(responseCode = "U009", description = "개인정보처리동의를 다시 받아야 합니다."),
            @ApiResponse(responseCode = "DB", description = "데이터베이스 오류입니다."),
            @ApiResponse(responseCode = "SERVER",description = "서버와의 연결에 실패했습니다.")
    })
    @PostMapping("/signin")
    public SingleResult<SignInResponseDto> signIn(@RequestBody @Valid SignInRequestDto dto){
        return responseService.getSingleResult(authService.signIn(dto));
    }

    @Operation(summary = "SNS 로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "SUCCESS", description = "응답 성공"),
            @ApiResponse(responseCode = "U004", description = "지원하지않는 oAuth 로그인 형식입니다."),
            @ApiResponse(responseCode = "DB", description = "데이터베이스 오류입니다."),
            @ApiResponse(responseCode = "SERVER",description = "서버와의 연결에 실패했습니다.")
    })
    @PostMapping("/sns-signin")
    public SingleResult<SnsSignInResponseDto> snsSignIn(@RequestBody @Valid SnsSignInRequestDto dto){
        return responseService.getSingleResult(authService.snsSignIn(dto));
    }


    @Operation(summary = "회원탈퇴")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "SUCCESS", description = "응답 성공"),
            @ApiResponse(responseCode = "U001", description = "존재하지 않는 유저입니다."),
            @ApiResponse(responseCode = "DB", description = "데이터베이스 오류입니다."),
            @ApiResponse(responseCode = "SERVER",description = "서버와의 연결에 실패했습니다.")
    })
    @PatchMapping("/status")
    public CommonResult deleteStatus(
            @AuthenticationPrincipal UserDetails userDetails
    ){
        authService.deleteStatus(userDetails.getUsername());
        return responseService.getSuccessResult();
    }

    @Operation(summary = "관리자 권한부여 및 관리자 로그인", description = "관리자 테스트를 원활히 할 수 있도록 만든 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "SUCCESS", description = "응답 성공"),
            @ApiResponse(responseCode = "U001", description = "존재하지 않는 유저입니다."),
            @ApiResponse(responseCode = "U003", description = "비밀번호가 일치하지 않습니다."),
            @ApiResponse(responseCode = "DB", description = "데이터베이스 오류입니다."),
            @ApiResponse(responseCode = "SERVER",description = "서버와의 연결에 실패했습니다.")
    })
    @PostMapping("/admin")
    public SingleResult<AdminStatusResponseDto> adminStatus(
            @RequestBody AdminStatusRequestDto dto
    ){

        return responseService.getSingleResult(authService.adminStatus(dto));
    }


}
