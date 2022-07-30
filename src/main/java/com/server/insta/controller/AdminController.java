package com.server.insta.controller;

import com.server.insta.config.response.ResponseService;
import com.server.insta.config.response.result.CommonResult;
import com.server.insta.config.response.result.SingleResult;
import com.server.insta.dto.request.AdminSignUpRequestDto;
import com.server.insta.dto.request.SignUpRequestDto;
import com.server.insta.dto.response.SignUpResponseDto;
import com.server.insta.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags="Admin API")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ResponseService responseService;
    private final AdminService adminService;

    @Operation(summary="관리자 회원가입")
    @PostMapping("/signup")
    public CommonResult signUp(
            @RequestBody AdminSignUpRequestDto dto
    ){
        adminService.signUp(dto);
        return responseService.getSuccessResult();
    }
}
