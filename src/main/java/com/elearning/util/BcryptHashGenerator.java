package com.elearning.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptHashGenerator {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Uso: java BcryptHashGenerator <senha>");
            System.exit(1);
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String senhaCodificada = encoder.encode(args[0]);
        System.out.println(senhaCodificada);
    }
}