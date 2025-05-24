package com.elearning.service;

import com.elearning.dto.AvaliacaoRequest;
import com.elearning.exception.CursoNaoEncontradoException;
import com.elearning.model.Avaliacao;
import com.elearning.model.Curso;
import com.elearning.model.Usuario;
import com.elearning.repository.mongo.AvaliacaoRepository;
import com.elearning.repository.mongo.CursoRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AvaliacaoService {
    private final AvaliacaoRepository avaliacaoRepository;
    private final CursoRepository cursoRepository;
    
    public Avaliacao avaliarCurso(String cursoId, AvaliacaoRequest request, Usuario usuario) {
    Curso curso = cursoRepository.findById(cursoId)
        .orElseThrow(() -> new CursoNaoEncontradoException(cursoId)); // Busca no MongoDB
    
    // Cálculo da nova média
    double novaMedia = (curso.getMediaAvaliacoes() * curso.getTotalAvaliacoes() + request.getNota()) 
        / (curso.getTotalAvaliacoes() + 1);
    
    curso.setMediaAvaliacoes(novaMedia);
    curso.setTotalAvaliacoes(curso.getTotalAvaliacoes() + 1);
    
    cursoRepository.save(curso); // Atualiza no MongoDB
    
    Avaliacao avaliacao = new Avaliacao(request.getNota(), request.getComentario(), curso, usuario);
    return avaliacaoRepository.save(avaliacao); // Salva no MongoDB
}
}