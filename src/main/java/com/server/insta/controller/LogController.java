package com.server.insta.controller;

import com.server.insta.config.response.ResponseService;
import com.server.insta.config.response.result.CommonResult;
import com.server.insta.config.response.result.MultipleResult;
import com.server.insta.dto.response.GetLogsResponseDto;
import com.server.insta.log.LogServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Api(tags="Log API")
@RestController
@RequestMapping("/api/log")
@RequiredArgsConstructor
public class LogController {

    private final ResponseService responseService;
    private final LogServiceImpl logService;

    @Operation(summary = "로그 조회", description = "로그는 페이징을 통해 조회 가능합니다. 페이지 인덱스는 0부터 시작합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "SUCCESS", description = "응답 성공"),
            @ApiResponse(responseCode = "U001", description = "존재하지 않는 유저입니다."),
            @ApiResponse(responseCode = "U008", description = "관리자 계정이 아닙니다."),
            @ApiResponse(responseCode = "ADM001", description = "날짜 형식이 올바르지 않습니다."),
            @ApiResponse(responseCode = "DB", description = "데이터베이스 오류입니다."),
            @ApiResponse(responseCode = "SERVER",description = "서버와의 연결에 실패했습니다.")
    })
    @GetMapping("")
    public MultipleResult<GetLogsResponseDto> getLogs(
            @AuthenticationPrincipal UserDetails userDetails,
            @Parameter(description = "시작날짜는 YYYY-MM-DD 이런 형식으로 보내주세요.") @RequestParam(required = false) String startDate,
            @Parameter(description = "종료날짜는 YYYY-MM-DD 이런 형식으로 보내주세요.") @RequestParam(required = false) String endDate,
            @Parameter(description = "페이징 피드 조회 인덱스, 0부터 시작합니다") @RequestParam int pageIndex,
            @Parameter(description = "페이징 피드 조회 사이즈") @RequestParam(value = "size") int pageSize
    ){
        return responseService.getMultipleResult(logService.getLogs(userDetails.getUsername(), startDate, endDate, pageIndex, pageSize));
    }

    @Operation(summary = "로그 전체 삭제", description = "DB에 저장되어있는 로그를 전부 지우는 용도입니다. 주의해서 사용해주세요.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "SUCCESS", description = "응답 성공"),
            @ApiResponse(responseCode = "U001", description = "존재하지 않는 유저입니다."),
            @ApiResponse(responseCode = "U008", description = "관리자 계정이 아닙니다."),
            @ApiResponse(responseCode = "DB", description = "데이터베이스 오류입니다."),
            @ApiResponse(responseCode = "SERVER",description = "서버와의 연결에 실패했습니다.")
    })
    @DeleteMapping("")
    public CommonResult deleteLogs(
            @AuthenticationPrincipal UserDetails userDetails
    ){
        logService.deleteLog(userDetails.getUsername());
        return responseService.getSuccessResult();
    }
}
