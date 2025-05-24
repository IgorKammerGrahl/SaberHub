package com.elearning.repository.mongo;

import com.elearning.model.Curso;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface CursoRepository extends MongoRepository<Curso, String> {
    List<Curso> findByCategoriasContaining(String categoria);
}