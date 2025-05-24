package com.elearning.repository.postgres;

import com.elearning.model.Inscricao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InscricaoRepository extends JpaRepository<Inscricao, Long> {
    boolean existsByUsuarioIdAndCursoId(Long usuarioId, String cursoId);
}