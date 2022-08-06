package com.server.insta.log;

import com.server.insta.config.response.ResponseService;
import com.server.insta.config.response.result.MultipleResult;
import com.server.insta.dto.response.GetLogsResponseDto;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
