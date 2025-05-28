package com.elearning.service;

import com.elearning.dto.CursoCriacaoDTO;
import com.elearning.model.Curso;
import com.elearning.model.Usuario; // Importar Usuario
import java.util.List;
import java.util.Optional;

public interface CursoService {
    Curso criarCurso(CursoCriacaoDTO cursoDTO, Usuario criador); // Adicionar Usuario criador
    List<Curso> listarTodos();
    Optional<Curso> buscarPorId(String id);
}