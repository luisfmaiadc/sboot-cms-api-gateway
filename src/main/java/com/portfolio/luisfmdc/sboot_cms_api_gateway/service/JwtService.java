package com.portfolio.luisfmdc.sboot_cms_api_gateway.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.portfolio.luisfmdc.sboot_cms_api_gateway.domain.Role;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class JwtService {

    private final String secret = "luisfmdc-cms";
    private final long expiration = 900000L;

    public Mono<String> gerarToken(String username, Role role) {
        return Mono.fromCallable(() ->
                JWT.create()
                        .withSubject(username)
                        .withClaim("role", role.name())
                        .withIssuedAt(new Date())
                        .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
                        .sign(Algorithm.HMAC256(secret))
        );
    }

    public Mono<DecodedJWT> validarToken(String token) {
        return Mono.fromCallable(() ->
                JWT.require(Algorithm.HMAC256(secret))
                        .build()
                        .verify(token)
        );
    }
}
