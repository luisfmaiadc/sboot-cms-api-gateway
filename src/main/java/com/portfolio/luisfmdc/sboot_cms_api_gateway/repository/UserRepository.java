package com.portfolio.luisfmdc.sboot_cms_api_gateway.repository;

import com.portfolio.luisfmdc.sboot_cms_api_gateway.domain.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends R2dbcRepository<User, Integer> {

    Mono<User> findByEmail(String email);
}
