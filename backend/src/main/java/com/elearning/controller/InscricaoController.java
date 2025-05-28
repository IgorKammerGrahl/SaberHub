package com.elearning.controller;

import com.elearning.exception.CursoNaoEncontradoException;
import com.elearning.exception.InscricaoDuplicadaException;
import com.elearning.model.Usuario;
import com.elearning.service.InscricaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/inscricoes") // Path base para operações de inscrição
public class InscricaoController {

    private static final Logger logger = LoggerFactory.getLogger(InscricaoController.class);

    @Autowired
    private InscricaoService inscricaoService;

    // Endpoint para processar o POST do formulário de matrícula
    @PostMapping("/matricular")
    public String matricularEmCurso(@RequestParam("cursoId") String cursoIdMongo,
                                    @AuthenticationPrincipal Usuario usuarioLogado, // Pega o usuário autenticado
                                    RedirectAttributes redirectAttributes) {

        if (usuarioLogado == null) {
            // Se o endpoint for protegido corretamente pelo Spring Security, isso não deve acontecer.
            // Spring Security redirecionará para o login se não autenticado.
            logger.warn("Tentativa de matrícula por usuário não autenticado para o curso ID: {}", cursoIdMongo);
            return "redirect:/login";
        }

        try {
            inscricaoService.realizarInscricao(usuarioLogado.getId(), cursoIdMongo);
            redirectAttributes.addFlashAttribute("matriculaSuccessMessage", "Matrícula realizada com sucesso!");
            logger.info("Usuário ID {} matriculado com sucesso no curso ID {}", usuarioLogado.getId(), cursoIdMongo);
        } catch (InscricaoDuplicadaException e) {
            redirectAttributes.addFlashAttribute("matriculaErrorMessage", e.getMessage());
            logger.warn("Falha na matrícula (duplicada) para Usuário ID {} no curso ID {}: {}", usuarioLogado.getId(), cursoIdMongo, e.getMessage());
        } catch (CursoNaoEncontradoException e) {
            redirectAttributes.addFlashAttribute("matriculaErrorMessage", e.getMessage());
            logger.error("Falha na matrícula - curso não encontrado. Curso ID {}: {}", cursoIdMongo, e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("matriculaErrorMessage", "Ocorreu um erro inesperado ao tentar realizar a matrícula.");
            logger.error("Erro inesperado na matrícula para Usuário ID {} no curso ID {}: {}", usuarioLogado.getId(), cursoIdMongo, e.getMessage(), e);
        }

        // Redireciona de volta para a página de detalhes do curso
        return "redirect:/cursos/" + cursoIdMongo;
    }
}