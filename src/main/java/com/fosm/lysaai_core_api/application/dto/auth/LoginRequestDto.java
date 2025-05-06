package com.fosm.lysaai_core_api.application.dto.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Builder
@Getter
public class LoginRequestDto {
    private String username;

    private String password;
}
