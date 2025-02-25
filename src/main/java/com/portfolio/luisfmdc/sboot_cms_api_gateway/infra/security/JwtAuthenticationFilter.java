package com.portfolio.luisfmdc.sboot_cms_api_gateway.infra.security;

import com.portfolio.luisfmdc.sboot_cms_api_gateway.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtAuthenticationFilter implements WebFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String token = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            return jwtService.validarToken(token)
                    .flatMap(decodedJWT -> {
                        String role = decodedJWT.getClaim("role").asString();
                        var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

                        var authentication = new UsernamePasswordAuthenticationToken(decodedJWT.getSubject(), null, authorities);
                        var securityContext = new SecurityContextImpl(authentication);

                        return chain.filter(exchange)
                                .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
                    })
                    .onErrorResume(e -> chain.filter(exchange));
        }
        return chain.filter(exchange);
    }
}
