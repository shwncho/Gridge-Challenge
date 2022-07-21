package com.server.insta.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class PostResponseDto {

    private String caption;

    private List<String> medias = new ArrayList<>();

    private List<String> tags = new ArrayList<>();
}
