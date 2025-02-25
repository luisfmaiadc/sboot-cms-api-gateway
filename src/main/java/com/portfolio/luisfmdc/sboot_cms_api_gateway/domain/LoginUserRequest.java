package com.portfolio.luisfmdc.sboot_cms_api_gateway.domain;

import lombok.Data;

@Data
public class LoginUserRequest {

    private String email;
    private String password;
}
