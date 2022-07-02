package com.server.insta.controller;

import com.server.insta.config.response.ResponseService;
import com.server.insta.config.response.result.SingleResult;
import com.server.insta.dto.LogInRequestDto;
import com.server.insta.dto.LogInResponseDto;
import com.server.insta.dto.SignUpRequestDto;
import com.server.insta.dto.SignUpResponseDto;
import com.server.insta.service.AuthService;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name="auth", description = "Auth API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final ResponseService responseService;

    @Operation(summary="회원가입")
    @PostMapping("/sign-up")
    public SingleResult<SignUpResponseDto> signUp(
            @ApiParam(value="회원 가입에 필요한 정보")
            @RequestBody @Valid SignUpRequestDto dto){
        return responseService.getSingleResult(authService.signUp(dto));
    }

    @Operation(summary="로그인")
    @PostMapping("/sign-in")
    public SingleResult<LogInResponseDto> signIn(@RequestBody @Valid LogInRequestDto dto){
        return responseService.getSingleResult(authService.signIn(dto));
    }



}
