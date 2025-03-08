package com.agprojeto.projetosapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.agprojeto.projetosapp.model.Projeto;

import jakarta.transaction.Transactional;

/**
 * Interface de repository para a classe Projeto, providência métodos CRUD por meio do JpaRepository
 */
public interface ProjetoRepository extends JpaRepository<Projeto, Integer> {

    @Query("SELECT p FROM Projeto p JOIN FETCH p.clienteParent")
    List<Projeto> findAllWithCliente();

    @Query("SELECT p FROM Projeto p JOIN FETCH p.clienteParent WHERE p.id = :id")
    Optional<Projeto> findByIdWithCliente(@Param("id") Integer id);

    @Query("SELECT p FROM Projeto p " +
            "JOIN FETCH p.clienteParent " +
            "LEFT JOIN FETCH p.atividadeList " +
            "WHERE p.id = :id")
    Optional<Projeto> findByIdClienteAtividades(@Param("id") Integer id);

    /**
     * Cancela atividades não finalizadas ou canceladas ao cancelar o processo
     * @param id processo sendo cancelado
     */
    @Modifying
    @Transactional
    @Query("UPDATE Atividade a SET a.status = 'CANCELADO', a.dataConclusao = CURRENT_TIMESTAMP " + 
                "where a.projetoParent.id = :id and (a.status = 'NOVO' or a.status = 'EM_DESENVOLVIMENTO')")
    void atualizaAtividadesOnCancelar(@Param("id") Integer id);

    /**
     * Cancela atividades não finalizadas ou canceladas ao finalizar o processo
     * @param id processo sendo cancelado
     */
    @Modifying
    @Transactional
    @Query("UPDATE Atividade a SET a.status = 'FINALIZADO', a.dataConclusao = CURRENT_TIMESTAMP " + 
        "where a.projetoParent.id = :id and (a.status = 'NOVO' or a.status = 'EM_DESENVOLVIMENTO')")
    void atualizaAtividadesOnFinalizar(@Param("id") Integer id);
}
