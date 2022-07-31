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
    @PostMapping("/signup")
    public SingleResult<SignUpResponseDto> signUp(
            @RequestBody @Valid SignUpRequestDto dto){
        return responseService.getSingleResult(authService.signUp(dto));
    }

    @Operation(summary="로그인")
    @PostMapping("/signin")
    public SingleResult<SignInResponseDto> signIn(@RequestBody @Valid SignInRequestDto dto){
        return responseService.getSingleResult(authService.signIn(dto));
    }

    @Operation(summary = "SNS 로그인")
    @PostMapping("/sns-signin")
    public SingleResult<SnsSignInResponseDto> snsSignIn(@RequestBody @Valid SnsSignInRequestDto dto){
        return responseService.getSingleResult(authService.snsSignIn(dto));
    }


    @Operation(summary = "회원탈퇴")
    @PatchMapping("/status")
    public CommonResult deleteStatus(
            @AuthenticationPrincipal UserDetails userDetails
    ){
        authService.deleteStatus(userDetails.getUsername());
        return responseService.getSuccessResult();
    }

    @Operation(summary = "관리자 권한부여")
    @PostMapping("/admin")
    public SingleResult<AdminStatusResponseDto> adminStatus(
            @RequestBody AdminStatusRequestDto dto
    ){

        return responseService.getSingleResult(authService.adminStatus(dto));
    }


}
