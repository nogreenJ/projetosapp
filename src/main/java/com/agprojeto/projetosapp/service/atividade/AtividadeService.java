package com.agprojeto.projetosapp.service.atividade;

import java.util.List;
import java.util.Optional;

import com.agprojeto.projetosapp.model.Atividade;

/**
 * Serviço que define métodos CRUD para Atividades
 */
public interface AtividadeService {
    
    /**
     * Salva um atividade
     * @param atividade Instância de atividade com todas as informações necessárias
     * @return O atividade persistido
     */
    Atividade saveAtividade(Atividade atividade);

    /**
     * Listagem de todos os atividades do banco
     * @return Lista de atividades
     */
    List<Atividade> listAtividades();

    /**
     * Busca os dados do atividade pelo seu id
     * @param atividadeId Identificador do atividade
     * @return O atividade encontrado, null se não encontrou
     */
    Optional<Atividade> findAtividadeById(Integer atividadeId);

    /**
     * Atualiza as informações de um atividade
     * @param atividade Atividade com os novos dados, é obrigatório que tenha o id informado
     * @return Atividade com os dados atualizados
     */
    Atividade updateAtividade(Atividade atividade);

    /**
     * Deleta um atividade do banco
     * @param atividadeId Identificador do atividade a ser deletado
     */
    void deleteAtividade(Integer atividadeId);
}
