package com.portfolio.luisfmdc.sboot_cms_api_gateway.domain;

import lombok.Data;

@Data
public class RegisterUserRequest {

    private String email;
    private String password;
    private Role role;
}
