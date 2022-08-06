package com.server.insta.log;

import com.server.insta.config.response.ResponseService;
import com.server.insta.config.response.result.CommonResult;
import com.server.insta.config.response.result.MultipleResult;
import com.server.insta.dto.response.GetLogsResponseDto;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    @DeleteMapping("")
    public CommonResult deleteLogs(
            @AuthenticationPrincipal UserDetails userDetails
    ){
        logService.deleteLog(userDetails.getUsername());
        return responseService.getSuccessResult();
    }
}
