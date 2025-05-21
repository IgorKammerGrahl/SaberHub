package com.elearning.repository;

import com.elearning.model.Inscricao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InscricaoRepository extends JpaRepository<Inscricao, Long> {
    boolean existsByUsuarioIdAndCursoId(Long usuarioId, String cursoId);
}