package com.elearning.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.elearning.service.UsuarioServiceImpl;

import java.io.IOException;

/**
 * Filtro JWT que autentica requisições API, ignorando caminhos públicos e páginas de formulário.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UsuarioServiceImpl usuarioService;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        // Se for um path público, não processa o filtro aqui
        if (shouldNotFilter(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = getTokenFromRequest(request);
        log.debug("🔑 Token recebido para {}: {}", request.getServletPath(), token);

        if (token != null && jwtUtil.validateToken(token)) {
            log.debug("✅ Token válido para {}", request.getServletPath());
            String username = jwtUtil.extractUsername(token);
            log.debug("👤 Usuário extraído: {}", username);

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = usuarioService.loadUserByUsername(username);
                if (userDetails != null) {
                    log.debug("🔐 Autoridades carregadas: {}", userDetails.getAuthorities());
                    UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                        );
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    log.info("Usuário {} autenticado via JWT para {}", username, request.getServletPath());
                } else {
                    log.warn("UserDetails não encontrado para o usuário {} do token JWT", username);
                }
            }
        } else {
            if (token == null) {
                log.trace("Nenhum token JWT encontrado na requisição para {}", request.getServletPath());
            } else {
                log.warn("❌ Token JWT inválido para {}: {}", request.getServletPath(), token);
            }
        }

        filterChain.doFilter(request, response);
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
        String path = request.getServletPath();
        String method = request.getMethod();

        // Paths que NÃO DEVEM ser processados pelo JwtFilter (públicos ou com formLogin)
        if (path.startsWith("/auth/") ||
            path.equals("/login") ||
            (path.equals("/register") && "GET".equals(method)) ||
            ("GET".equals(method) && (
                path.equals("/") ||
                path.startsWith("/cursos") ||
                path.startsWith("/css/") ||
                path.startsWith("/js/") ||
                path.startsWith("/images/") ||
                path.equals("/favicon.ico")
            )) ||
            path.startsWith("/actuator") ||
            path.startsWith("/swagger-ui") ||
            path.startsWith("/v3/api-docs") ||
            path.startsWith("/webjars/")) {
            log.trace("JwtFilter NÃO será aplicado para o path: {} {}", method, path);
            return true;
        }

        log.trace("JwtFilter SERÁ aplicado para o path: {} {}", method, path);
        return false;
    }
}
