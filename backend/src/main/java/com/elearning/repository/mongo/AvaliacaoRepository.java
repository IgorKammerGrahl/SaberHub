package com.elearning.repository.mongo;

import com.elearning.model.Avaliacao;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface AvaliacaoRepository extends MongoRepository<Avaliacao, String> {
    List<Avaliacao> findByCursoId(String cursoId);
}