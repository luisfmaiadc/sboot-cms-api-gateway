package com.portfolio.luisfmdc.sboot_cms_api_gateway.controller;

import com.portfolio.luisfmdc.sboot_cms_api_gateway.domain.LoginUserRequest;
import com.portfolio.luisfmdc.sboot_cms_api_gateway.domain.LoginUserResponse;
import com.portfolio.luisfmdc.sboot_cms_api_gateway.domain.RegisterUserRequest;
import com.portfolio.luisfmdc.sboot_cms_api_gateway.service.AuthenticationService;
import com.portfolio.luisfmdc.sboot_cms_api_gateway.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Mono<ResponseEntity<LoginUserResponse>> login(@RequestBody LoginUserRequest request) {
        return authenticationService.autenticar(request.getEmail(), request.getPassword())
                .map(token -> ResponseEntity.ok().body(new LoginUserResponse(token)))
                .defaultIfEmpty(ResponseEntity.status(401).build());
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<Void>> registro(@RequestBody RegisterUserRequest request) {
        return userService.registerUser(request)
                .then(Mono.just(ResponseEntity.status(HttpStatus.CREATED).build()));
    }
}
