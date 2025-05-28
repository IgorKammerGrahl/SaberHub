package com.elearning.controller;

import com.elearning.dto.CursoCriacaoDTO;
import com.elearning.model.Curso;
import com.elearning.model.Usuario;
import com.elearning.service.CursoService;
import com.elearning.service.InscricaoService;
import com.elearning.service.UsuarioService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cursos")
public class CursoPageController {

    private static final Logger logger = LoggerFactory.getLogger(CursoPageController.class);

    @Autowired
    private CursoService cursoService;

    @Autowired
    private InscricaoService inscricaoService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String listarCursosPage(Model model) {
        logger.info("Acessando página de listagem de cursos.");
        List<Curso> listaDeCursos = cursoService.listarTodos();
        model.addAttribute("cursos", listaDeCursos);
        if (listaDeCursos.isEmpty()) {
            model.addAttribute("mensagemInfo", "Nenhum curso encontrado no momento.");
        }
        return "cursos";
    }

    @GetMapping("/{id}")
    public String detalheCursoPage(@PathVariable String id, Model model,
                                   @AuthenticationPrincipal Usuario usuarioLogado) {
        logger.info("Acessando página de detalhes para o curso ID: {}", id);
        Optional<Curso> cursoOpt = cursoService.buscarPorId(id);
        if (cursoOpt.isPresent()) {
            Curso curso = cursoOpt.get();
            model.addAttribute("curso", curso);

            boolean isJaInscrito = false;
            if (usuarioLogado != null) {
                isJaInscrito = inscricaoService.verificarInscricao(usuarioLogado.getId(), curso.getId());
            }
            model.addAttribute("isJaInscrito", isJaInscrito);

            return "detalhe-curso";
        } else {
            logger.warn("Curso com ID: {} não encontrado.", id);
            model.addAttribute("errorMessage", "Curso não encontrado.");
            return "error/404";
        }
    }

    // Exibe formulário de criação de curso
    @GetMapping("/novo")
    public String mostrarFormularioCriarCurso(Model model) {
        logger.info("Acessando formulário para criar novo curso.");
        // Inicializa DTO com campos em branco
        model.addAttribute("cursoCriacaoDTO", new CursoCriacaoDTO("", "", ""));
        return "criar-curso";
    }

    // Processa submissão do formulário de criação
    @PostMapping("/novo")
    public String processarCriacaoCurso(
            @Valid @ModelAttribute("cursoCriacaoDTO") CursoCriacaoDTO cursoDTO,
            BindingResult bindingResult,
            @AuthenticationPrincipal Usuario usuarioLogado,
            RedirectAttributes redirectAttributes,
            Model model) {

        logger.info("Processando criação de curso: {}", cursoDTO.getTitulo());

        if (usuarioLogado == null) {
            logger.warn("Usuário não autenticado tentou criar curso.");
            return "redirect:/login";
        }

        // Processar categorias separadas por vírgula do input
        if (cursoDTO.getCategoriasInput() != null && !cursoDTO.getCategoriasInput().isBlank()) {
            List<String> categoriasList = Arrays.stream(cursoDTO.getCategoriasInput().split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
            if (categoriasList.isEmpty()) {
                bindingResult.rejectValue("categoriasInput", "NotEmpty.cursoCriacaoDTO.categoriasInput", "Pelo menos uma categoria válida é obrigatória.");
            }
            cursoDTO.setCategorias(categoriasList);
        } else {
            bindingResult.rejectValue("categoriasInput", "NotEmpty.cursoCriacaoDTO.categoriasInput", "Pelo menos uma categoria é obrigatória.");
        }

        // Verifica erros de validação gerais
        if (bindingResult.hasErrors()) {
            logger.warn("Erros de validação ao criar curso: {}", bindingResult.getAllErrors());
            return "criar-curso";
        }

        try {
            Curso novoCurso = cursoService.criarCurso(cursoDTO, usuarioLogado);
            logger.info("Curso criado com sucesso: {}", novoCurso.getId());
            redirectAttributes.addFlashAttribute("successMessage", "Curso '" + novoCurso.getTitulo() + "' criado com sucesso!");
            return "redirect:/cursos/" + novoCurso.getId();
        } catch (Exception e) {
            logger.error("Erro ao criar curso: {}", e.getMessage(), e);
            model.addAttribute("formErrorMessage", "Erro ao criar curso: " + e.getMessage());
            return "criar-curso";
        }
    }
}