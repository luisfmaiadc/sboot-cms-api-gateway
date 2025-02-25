package com.portfolio.luisfmdc.sboot_cms_api_gateway.service;

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

    public Mono<String> autenticar(String email, String password) {
        return userRepository.findByEmail(email)
                .flatMap(user -> {
                    if (passwordEncoder.matches(password, user.getPassword())) {
                        return jwtService.gerarToken(user.getEmail(), user.getRole());
                    }
                    return Mono.error(new RuntimeException("Credenciais inválidas."));
                });
    }
}
