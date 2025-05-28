package com.elearning.security;

import com.elearning.model.Role;
import com.elearning.model.Usuario;
import com.elearning.repository.postgres.UsuarioRepository;
import com.elearning.security.jwt.JwtUtil;
import com.elearning.security.payload.LoginRequest;
import com.elearning.security.payload.LoginResponse;
import com.elearning.security.payload.MessageResponse;
import com.elearning.security.payload.RegistroRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger; // Adicionado Logger
import org.slf4j.LoggerFactory; // Adicionado Logger
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller; // MUDADO PARA CONTROLLER
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller // MUDADO: Este controller agora pode retornar nomes de view (para erros de registro)
@RequestMapping("/auth") // Path base SEM /api/
// @CrossOrigin(origins = "*", maxAge = 3600) // Pode ser desnecessário se CORS global no SecurityConfig
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class); // Logger

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Endpoint para processar o POST do formulário de registro Thymeleaf
    @PostMapping("/register")
    public String processRegistration(@Valid @ModelAttribute("registroRequest") RegistroRequest registroRequest,
                                      BindingResult bindingResult,
                                      @RequestParam("confirmarSenha") String confirmarSenha, // Pega do formulário
                                      RedirectAttributes redirectAttributes,
                                      Model model) {

        logger.info("Processando requisição de registro para: {}", registroRequest.email());

        boolean passwordsDoNotMatch = !registroRequest.senha().equals(confirmarSenha);
        if (passwordsDoNotMatch) {
            // Adiciona um erro ao model para ser exibido no template
            // O BindingResult não tem um campo "confirmarSenha" para adicionar erro diretamente
            model.addAttribute("passwordMismatchError", "As senhas não coincidem.");
            logger.warn("Falha no registro - Senhas não coincidem para: {}", registroRequest.email());
        }

        if (bindingResult.hasErrors() || passwordsDoNotMatch) {
            logger.warn("Falha na validação do registro para: {}. Erros: {}", registroRequest.email(), bindingResult.getAllErrors());
            // O objeto registroRequest já está no model devido ao @ModelAttribute
            // e o passwordMismatchError foi adicionado acima se necessário.
            return "register"; // Retorna para a página de registro para mostrar os erros
        }

        if (usuarioRepository.existsByEmail(registroRequest.email())) {
            logger.warn("Falha no registro - Email já em uso: {}", registroRequest.email());
            model.addAttribute("errorMessage", "Erro: Email já está em uso!");
            // O objeto registroRequest já está no model
            return "register";
        }

        Usuario novoUsuario = new Usuario(
                registroRequest.email(),
                passwordEncoder.encode(registroRequest.senha()),
                Role.USER
        );
        // Se tivesse nome: novoUsuario.setNome(registroRequest.nome());

        usuarioRepository.save(novoUsuario);
        logger.info("Usuário registrado com sucesso: {}", novoUsuario.getEmail());

        redirectAttributes.addFlashAttribute("registrationSuccess", "Usuário registrado com sucesso! Faça login.");
        return "redirect:/login?registered=true"; // Redireciona para login
    }


    // Endpoint de API para login (se você precisar de um login via JS/SPA no futuro)
    // O login via formulário Thymeleaf é tratado pelo Spring Security através de .formLogin()
    @PostMapping("/login-api")
    @ResponseBody // Importante para retornar JSON em um @Controller
    public ResponseEntity<?> loginApi(@Valid @RequestBody LoginRequest loginRequest) { // @RequestBody para JSON
        logger.info("Requisição de login API para: {}", loginRequest.email());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.senha())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            Usuario userPrincipal = (Usuario) authentication.getPrincipal();
            String jwt = jwtUtil.generateToken(userPrincipal);

            List<String> roles = userPrincipal.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            
            logger.info("Login API bem-sucedido para: {}", loginRequest.email());
            return ResponseEntity.ok(new LoginResponse(
                    jwt,
                    userPrincipal.getId(),
                    userPrincipal.getEmail(),
                    roles
            ));
        } catch (Exception e) {
            logger.error("Falha no login API para: {}: {}", loginRequest.email(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Credenciais inválidas"));
        }
    }

    // Endpoint de API para validar token
    @GetMapping("/validate")
    @ResponseBody // Importante para retornar JSON
    public ResponseEntity<?> validateToken(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            logger.warn("Tentativa de validar token inválido ou sessão expirada.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Token inválido ou sessão expirada."));
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        logger.info("Token validado para usuário: {}", userDetails.getUsername());
        return ResponseEntity.ok(Map.of(
                "username", userDetails.getUsername(),
                "authorities", userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList())
        ));
    }
}