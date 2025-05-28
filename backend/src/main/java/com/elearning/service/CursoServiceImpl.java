package com.elearning.service.impl;

import com.elearning.dto.CursoCriacaoDTO;
import com.elearning.model.Curso;
import com.elearning.model.Usuario;
import com.elearning.repository.mongo.CursoRepository;
import com.elearning.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CursoServiceImpl implements CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Override
    public Curso criarCurso(CursoCriacaoDTO cursoDTO, Usuario criador) {
        Curso novoCurso = new Curso();
        novoCurso.setTitulo(cursoDTO.getTitulo());
        novoCurso.setDescricao(cursoDTO.getDescricao());
        novoCurso.setCategorias(cursoDTO.getCategorias());
        novoCurso.setCriadorId(criador.getId());
        novoCurso.setCriadorNome(criador.getEmail()); // Ou nome, se tiver um campo nome no Usuario
        novoCurso.setDataCriacao(new Date()); // Já está no construtor padrão, mas pode ser explícito

        // Setar campos opcionais do DTO
        if (cursoDTO.getImagemUrl() != null) novoCurso.setImagemUrl(cursoDTO.getImagemUrl());
        if (cursoDTO.getDuracao() != null) novoCurso.setDuracao(cursoDTO.getDuracao());
        if (cursoDTO.getNivel() != null) novoCurso.setNivel(cursoDTO.getNivel());
        if (cursoDTO.getPreco() != null) novoCurso.setPreco(cursoDTO.getPreco());


        return cursoRepository.save(novoCurso);
    }

    @Override
    public List<Curso> listarTodos() {
        return cursoRepository.findAll();
    }

    @Override
    public Optional<Curso> buscarPorId(String id) {
        return cursoRepository.findById(id);
    }
}