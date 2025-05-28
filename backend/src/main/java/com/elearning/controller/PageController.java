package com.elearning.controller;

import com.elearning.security.payload.RegistroRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("tituloBoasVindas", "Bem-vindo à Nossa Plataforma de E-learning!");
        model.addAttribute("mensagemBoasVindas", "Descubra um mundo de conhecimento ao seu alcance.");
        return "home";  // templates/home.html
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            @RequestParam(value = "registered", required = false) String registered,
                            Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            return "redirect:/"; // Usuário já autenticado
        }
        if (error != null) {
            model.addAttribute("loginError", "Email ou senha inválidos.");
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "Você foi desconectado com sucesso.");
        }
        if (registered != null) {
            // mensagem flash registrada pelo AuthController
            // se necessário, pode fazer:
            // model.addAttribute("registrationSuccessMessage", "Usuário registrado com sucesso! Faça login.");
        }
        return "login"; // templates/login.html
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            return "redirect:/"; // Já logado vai para home
        }
        model.addAttribute("registroRequest", new RegistroRequest("", ""));
        return "register"; // templates/register.html
    }
}
