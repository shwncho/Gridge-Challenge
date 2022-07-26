package com.server.insta.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GetCommentsResponseDto {

    private Long commentId;
    private String content;
    private Long userId;
    private String nickName;
    private String profileImgUrl;
    private List<GetCommentsResponseDto> children = new ArrayList<>();

    @Builder
    public GetCommentsResponseDto(Long commentId, String content, Long userId, String nickName, String profileImgUrl){
        this.commentId = commentId;
        this.content = content;
        this.userId = userId;
        this.nickName = nickName;
        this.profileImgUrl = profileImgUrl;
    }


}
