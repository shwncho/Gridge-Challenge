package com.server.insta.controller;

import com.server.insta.config.response.ResponseService;
import com.server.insta.config.response.result.CommonResult;
import com.server.insta.config.response.result.SingleResult;
import com.server.insta.dto.request.SendMessageRequestDto;
import com.server.insta.dto.response.GetChattingResponseDto;
import com.server.insta.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags="Message API")
@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController {

    private final ResponseService responseService;
    private final MessageService messageService;

    @Operation(summary = "메세지 보내기")
    @PostMapping("/{receiverId}")
    public CommonResult sendMessage(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long receiverId,
            @RequestBody @Valid SendMessageRequestDto dto
    ){
        messageService.sendMessage(userDetails.getUsername(), receiverId, dto);
        return responseService.getSuccessResult();
    }

    @Operation(summary = "메세지 조회", description = "나와 채팅을 주고받은 유저의 id값을 넣어주면 됩니다.")
    @GetMapping("/{userId}")
    public SingleResult<GetChattingResponseDto> getChatting(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long userId
    ){
        return responseService.getSingleResult(messageService.getChatting(userDetails.getUsername(), userId));
    }
}
