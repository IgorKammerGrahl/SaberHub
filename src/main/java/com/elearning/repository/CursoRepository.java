package com.elearning.repository;

import com.elearning.model.Curso;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface CursoRepository extends MongoRepository<Curso, String> {
    // Métodos adicionais podem ser declarados aqui
    List<Curso> findByCategoriasContaining(String categoria);
}