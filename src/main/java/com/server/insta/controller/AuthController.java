package com.server.insta.controller;

import com.server.insta.config.response.ResponseService;
import com.server.insta.config.response.result.SingleResult;
import com.server.insta.dto.request.SignInRequestDto;
import com.server.insta.dto.request.SnsSignInRequestDto;
import com.server.insta.dto.response.SignInResponseDto;
import com.server.insta.dto.request.SignUpRequestDto;
import com.server.insta.dto.response.SignUpResponseDto;
import com.server.insta.dto.response.SnsSignInResponseDto;
import com.server.insta.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
            @ApiParam(value="회원 가입에 필요한 정보")
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




}
