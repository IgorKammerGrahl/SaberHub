package com.elearning.service;

import com.elearning.model.*;
import com.elearning.repository.mongo.CursoRepository;
import com.elearning.repository.postgres.InscricaoRepository;
import com.elearning.repository.postgres.UsuarioRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import com.elearning.exception.UsuarioNaoEncontradoException;
import com.elearning.exception.InscricaoDuplicadaException;
import com.elearning.exception.CursoNaoEncontradoException;

@Service
@RequiredArgsConstructor 
public class InscricaoService {
    private final CursoRepository cursoRepository;
    private final InscricaoRepository inscricaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Inscricao inscrever(String cursoId, Long usuarioId) throws JsonProcessingException {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioId));

        // Validação do curso (sem armazenar o objeto)
        buscarCursoComCache(cursoId); 

        if (inscricaoRepository.existsByUsuarioIdAndCursoId(usuarioId, cursoId)) {
            throw new InscricaoDuplicadaException(usuarioId, cursoId);
        }

        return criarInscricao(usuario, cursoId);
    }

    private Curso buscarCursoComCache(String cursoId) throws JsonProcessingException {
        String cursoKey = "curso:" + cursoId;
        String cursoJson = redisTemplate.opsForValue().get(cursoKey);
        
        if (cursoJson != null) {
            return objectMapper.readValue(cursoJson, Curso.class);
        }
        
        Curso curso = cursoRepository.findById(cursoId)
            .orElseThrow(() -> new CursoNaoEncontradoException(cursoId));
        
        redisTemplate.opsForValue().set(
            cursoKey, 
            objectMapper.writeValueAsString(curso), 
            1, TimeUnit.HOURS
        );
        
        return curso;
    }

    private Inscricao criarInscricao(Usuario usuario, String cursoId) {
        Inscricao inscricao = new Inscricao();
        inscricao.setUsuario(usuario);
        inscricao.setCursoId(cursoId);
        inscricao.setStatus(Inscricao.StatusInscricao.ATIVA);
        inscricao.setDataInscricao(LocalDateTime.now());
        return inscricaoRepository.save(inscricao);
    }
}