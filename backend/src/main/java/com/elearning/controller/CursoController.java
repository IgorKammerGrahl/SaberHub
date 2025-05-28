package com.elearning.controller;

import com.elearning.dto.CursoCriacaoDTO;
import com.elearning.model.Curso;
import com.elearning.model.Usuario; // Importar Usuario
import com.elearning.service.CursoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal; // Para injetar o usuário logado
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    // Endpoint para criar curso (protegido - requer autenticação)
    @PostMapping
    public ResponseEntity<Curso> criarCurso(@Valid @RequestBody CursoCriacaoDTO cursoDTO,
                                          @AuthenticationPrincipal Usuario usuarioLogado) {
        if (usuarioLogado == null) {
            // Isso não deve acontecer se o Spring Security estiver configurado corretamente
            // para proteger este endpoint
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Curso novoCurso = cursoService.criarCurso(cursoDTO, usuarioLogado);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCurso);
    }

    // Endpoint para listar todos os cursos (público)
    @GetMapping
    public ResponseEntity<List<Curso>> listarCursos() {
        List<Curso> cursos = cursoService.listarTodos();
        return ResponseEntity.ok(cursos);
    }

    // Endpoint para buscar curso por ID (público)
    @GetMapping("/{id}")
    public ResponseEntity<Curso> buscarCursoPorId(@PathVariable String id) {
        return cursoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}