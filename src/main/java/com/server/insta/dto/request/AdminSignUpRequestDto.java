package com.server.insta.dto.request;

import com.server.insta.domain.Admin;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminSignUpRequestDto {

    private String adminId;

    private String password;

    private String adminName;

    public Admin toEntity(){
        return Admin.builder()
                .adminId(this.adminId)
                .password(this.password)
                .adminName(this.adminName)
                .build();
    }
}
