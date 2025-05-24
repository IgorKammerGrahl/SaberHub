// src/main/java/com/elearning/controller/AvaliacaoController.java
package com.elearning.controller;

import com.elearning.dto.AvaliacaoRequest;
import com.elearning.model.Avaliacao;
import com.elearning.service.AvaliacaoService;
import com.elearning.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.elearning.model.Usuario; 

@RestController
@RequestMapping("/api/cursos/{cursoId}/avaliacoes")
@RequiredArgsConstructor
public class AvaliacaoController {
    private final AvaliacaoService avaliacaoService;
    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Avaliacao> avaliarCurso(
        @PathVariable String cursoId,
        @RequestBody AvaliacaoRequest request,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        Usuario usuario = usuarioService.buscarPorEmail(userDetails.getUsername());
        Avaliacao avaliacao = avaliacaoService.avaliarCurso(cursoId, request, usuario);
        return ResponseEntity.ok(avaliacao);
    }
}