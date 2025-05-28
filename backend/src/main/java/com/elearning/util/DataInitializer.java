package com.elearning.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;         // <— adicione
import org.springframework.beans.factory.annotation.Value; // <— adicione
import org.springframework.security.crypto.password.PasswordEncoder;

import com.elearning.model.Usuario;
import com.elearning.model.Role;                           // <— adicione
import com.elearning.repository.postgres.UsuarioRepository;

@Configuration
@Profile("docker")
public class DataInitializer {

    @Value("${ADMIN_EMAIL:admin@email.com}")
    private String adminEmail;

    @Value("${ADMIN_PASSWORD:senha123}")
    private String rawPassword;

    @Bean
    public CommandLineRunner seedAdmin(
        UsuarioRepository userRepo,
        PasswordEncoder passwordEncoder
    ) {
        return args -> {
            if (!userRepo.existsByEmail(adminEmail)) {
                Usuario admin = new Usuario(
                    adminEmail,
                    passwordEncoder.encode(rawPassword),
                    Role.ADMIN
                );
                userRepo.save(admin);
                System.out.println("Admin user created: " + adminEmail);
            }
        };
    }
}
