package com.elearning.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Claims;
import java.io.IOException;
import java.util.List;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.Collections; 

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

     @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request, 
        @NonNull HttpServletResponse response, 
        @NonNull FilterChain chain
    ) throws ServletException, IOException {
        
        String token = getTokenFromRequest(request);
        if (token != null && jwtUtil.validateToken(token)) {
            Claims claims = jwtUtil.extractAllClaims(token);
            String username = claims.getSubject();
            
            @SuppressWarnings("unchecked")
            List<String> roles = claims.get("roles", List.class);
            if (roles == null) {
                roles = Collections.emptyList(); // Evita NullPointerException
            }

            Collection<SimpleGrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role)) // Prefixo padrão do Spring
                .collect(Collectors.toList());
            
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                username, null, authorities
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        chain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        // Ignora rotas públicas (ex: login, registro)
        return request.getServletPath().startsWith("/api/auth/");
    }
}