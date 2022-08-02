package com.server.insta.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
public class UpdatePostRequestDto {

    @Schema(description = "게시물 설명")
    @Size(max=1000, message = "게시물 설명은 최대 1000자 까지만 가능합니다.")
    private String caption;

    @Schema(description = "수정해서 새로 작성할 해시태그, 수정할 것 없으면 변경 안해도 됨")
    private List<String> tags = new ArrayList<>();

    @Schema(description = "수정한 뒤 새로 작성할 이미지, 수정할 것 없으면 변경 안해도 됨")
    private List<String> medias = new ArrayList<>();
}
