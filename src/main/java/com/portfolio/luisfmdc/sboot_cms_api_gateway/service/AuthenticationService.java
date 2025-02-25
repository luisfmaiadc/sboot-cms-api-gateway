package com.portfolio.luisfmdc.sboot_cms_api_gateway.service;

import com.portfolio.luisfmdc.sboot_cms_api_gateway.infra.exception.InvalidRequestArgumentsException;
import com.portfolio.luisfmdc.sboot_cms_api_gateway.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private final String errorMessage = "Erro ao obter token, credenciais inválidas.";

    public Mono<String> autenticar(String email, String password) {
        return userRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new InvalidRequestArgumentsException(errorMessage)))
                .flatMap(user -> {
                    if (passwordEncoder.matches(password, user.getPassword())) {
                        return jwtService.gerarToken(user.getEmail(), user.getRole());
                    }
                     return Mono.error(new InvalidRequestArgumentsException(errorMessage));
                });
    }
}
