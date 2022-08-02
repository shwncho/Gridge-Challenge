package com.server.insta.dto.request;

import com.server.insta.config.Entity.Status;
import com.server.insta.domain.Post;
import com.server.insta.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
public class CreatePostRequestDto {

    @Schema(description = "게시물 설명")
    @Size(max=1000, message = "게시물 설명은 최대 1000자 까지만 가능합니다.")
    private String caption;

    @Schema(description = "게시물 해시태그")
    private List<String> tags = new ArrayList<>();

    @Schema(description = "게시물 이미지들")
    private List<String> medias = new ArrayList<>();


    public Post toEntity(User user){
        return Post.builder()
                .caption(this.caption)
                .user(user)
                .status(Status.ACTIVE)
                .build();
    }


}
