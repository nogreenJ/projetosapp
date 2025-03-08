package com.agprojeto.projetosapp.service.projeto;

import java.util.List;
import java.util.Optional;

import com.agprojeto.projetosapp.model.Projeto;

/**
 * Serviço que define métodos CRUD para Projetos
 */
public interface ProjetoService {
    
    /**
     * Salva um projeto
     * @param projeto Instância de projeto com todas as informações necessárias
     * @return O projeto persistido
     */
    Projeto saveProjeto(Projeto projeto);

    /**
     * Listagem de todos os projetos do banco
     * @return Lista de projetos
     */
    List<Projeto> listProjetos();

    /**
     * Busca os dados do projeto pelo seu id
     * @param projetoId Identificador do projeto
     * @return O projeto encontrado, null se não encontrou
     */
    Optional<Projeto> findProjetoById(Integer projetoId);

    /**
     * Atualiza as informações de um projeto
     * @param projeto Projeto com os novos dados, é obrigatório que tenha o id informado
     * @return Projeto com os dados atualizados
     */
    Projeto updateProjeto(Projeto projeto);

    /**
     * Deleta um projeto do banco
     * @param projetoId Identificador do projeto a ser deletado
     */
    void deleteProjeto(Integer projetoId);
}
