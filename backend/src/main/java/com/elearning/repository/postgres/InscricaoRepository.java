package com.elearning.repository.postgres;

import com.elearning.model.Inscricao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InscricaoRepository extends JpaRepository<Inscricao, Long> {

    // Adicione ou modifique para este método:
    boolean existsByUsuarioIdAndCursoIdMongo(Long usuarioId, String cursoIdMongo);

    // Outros métodos que você possa ter...
}