package com.elearning.service.impl;

import com.elearning.exception.CursoNaoEncontradoException;
import com.elearning.exception.InscricaoDuplicadaException;
import com.elearning.model.Curso;
import com.elearning.model.Inscricao;
import com.elearning.model.Usuario;
import com.elearning.repository.mongo.CursoRepository; // Para verificar se o curso existe
import com.elearning.repository.postgres.InscricaoRepository;
import com.elearning.repository.postgres.UsuarioRepository; // Para buscar o objeto Usuario
import com.elearning.service.InscricaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importante para operações de escrita

import java.time.LocalDateTime;

@Service
public class InscricaoServiceImpl implements InscricaoService {

    private static final Logger logger = LoggerFactory.getLogger(InscricaoServiceImpl.class);

    @Autowired
    private InscricaoRepository inscricaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository; // Para buscar o objeto Usuario

    @Autowired
    private CursoRepository cursoRepository; // Para verificar se o curso do MongoDB existe

    @Override
    @Transactional // Garante atomicidade na operação de inscrição
    public Inscricao realizarInscricao(Long usuarioId, String cursoIdMongo)
            throws InscricaoDuplicadaException, CursoNaoEncontradoException {

        // 1. Verificar se o curso existe no MongoDB
        if (!cursoRepository.existsById(cursoIdMongo)) {
            logger.warn("Tentativa de inscrição em curso inexistente. Curso ID: {}", cursoIdMongo);
            throw new CursoNaoEncontradoException("Curso com ID " + cursoIdMongo + " não encontrado.");
        }

        // 2. Verificar se o usuário já está inscrito neste curso
        if (inscricaoRepository.existsByUsuarioIdAndCursoIdMongo(usuarioId, cursoIdMongo)) {
            logger.warn("Tentativa de inscrição duplicada. Usuário ID: {}, Curso ID: {}", usuarioId, cursoIdMongo);
            throw new InscricaoDuplicadaException("Usuário já inscrito neste curso.");
        }

        // 3. Buscar o objeto Usuario (se ele não existir, o Spring Security já teria bloqueado,
        // mas é uma boa prática verificar ou usar uma referência)
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário com ID " + usuarioId + " não encontrado.")); // Deve ser raro

        // 4. Criar e salvar a nova inscrição
        Inscricao novaInscricao = new Inscricao();
        novaInscricao.setUsuario(usuario);
        novaInscricao.setCursoIdMongo(cursoIdMongo);
        novaInscricao.setStatus(Inscricao.StatusInscricao.ATIVA);
        // dataInscricao será setada pelo @PrePersist na entidade Inscricao

        Inscricao inscricaoSalva = inscricaoRepository.save(novaInscricao);
        logger.info("Inscrição realizada com sucesso. Usuário ID: {}, Curso ID: {}", usuarioId, cursoIdMongo);
        return inscricaoSalva;
    }

    @Override
    public boolean verificarInscricao(Long usuarioId, String cursoIdMongo) {
        return inscricaoRepository.existsByUsuarioIdAndCursoIdMongo(usuarioId, cursoIdMongo);
    }
}