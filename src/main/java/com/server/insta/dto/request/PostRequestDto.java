package com.server.insta.dto.request;

import com.server.insta.domain.Post.Post;
import com.server.insta.domain.User.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class    PostRequestDto {

    @NotNull
    @Schema(description = "게시물 설명")
    private String caption;

    private List<String> tags = new ArrayList<>();

    @NotNull(message = "이미지를 최소 1개이상 업로드 해야합니다.")
    private List<String> medias = new ArrayList<>();


    public Post toEntity(User user){
        return Post.builder()
                .user(user)
                .caption(this.caption)
                .build();
    }
}
