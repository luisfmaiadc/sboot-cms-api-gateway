package com.portfolio.luisfmdc.sboot_cms_api_gateway.service;

import com.portfolio.luisfmdc.sboot_cms_api_gateway.domain.RegisterUserRequest;
import com.portfolio.luisfmdc.sboot_cms_api_gateway.domain.Role;
import com.portfolio.luisfmdc.sboot_cms_api_gateway.domain.User;
import com.portfolio.luisfmdc.sboot_cms_api_gateway.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Mono<Void> registerUser(RegisterUserRequest request) {
        if (request.getRole() == Role.ADMIN) {
            return Mono.error(new IllegalArgumentException("Cadastro de ADMIN não permitido!"));
        }

        return repository.findByEmail(request.getEmail())
                .flatMap(existingUser -> Mono.error(new IllegalArgumentException("Email já cadastrado!")))
                .switchIfEmpty(Mono.defer(() -> {
                    String encryptedPassword = passwordEncoder.encode(request.getPassword());
                    User user = User.builder()
                            .email(request.getEmail())
                            .password(encryptedPassword)
                            .role(request.getRole())
                            .build();
                    return repository.save(user).then();
                })).then();
    }
}
