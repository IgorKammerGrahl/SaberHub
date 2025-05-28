package com.elearning.service;

import com.elearning.model.Inscricao;
import com.elearning.model.Usuario;
import com.elearning.exception.InscricaoDuplicadaException; // Crie esta exceção
import com.elearning.exception.CursoNaoEncontradoException; // Crie esta exceção (se CursoService não a tiver)

public interface InscricaoService {
    Inscricao realizarInscricao(Long usuarioId, String cursoIdMongo)
        throws InscricaoDuplicadaException, CursoNaoEncontradoException;

    boolean verificarInscricao(Long usuarioId, String cursoIdMongo);
}