package com.elearning.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String senhaCodificada = encoder.encode("senha123");
        System.out.println("Hash BCrypt: " + senhaCodificada);
    }
}