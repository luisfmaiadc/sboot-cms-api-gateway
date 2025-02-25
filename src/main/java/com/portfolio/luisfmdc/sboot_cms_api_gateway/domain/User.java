package com.portfolio.luisfmdc.sboot_cms_api_gateway.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("users")
@Getter
@Builder
public class User {

    @Id
    private Integer id;

    private String email;
    private String password;
    private Role role;
}
