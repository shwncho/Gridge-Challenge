package com.server.insta.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommetsByPostInfoDto {

    private String username;

    private String content;
}
