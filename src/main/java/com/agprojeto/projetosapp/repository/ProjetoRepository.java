package com.agprojeto.projetosapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.agprojeto.projetosapp.model.Projeto;

/**
 * Interface de repository para a classe Projeto, provicência métodos CRUD por meio do JpaRepository
 */
public interface ProjetoRepository extends JpaRepository<Projeto, Integer> {

    @Query("SELECT p FROM Projeto p JOIN FETCH p.clienteParent")
    List<Projeto> findAllWithCliente();

    @Query("SELECT p FROM Projeto p JOIN FETCH p.clienteParent WHERE p.id = :id")
    Optional<Projeto> findByIdWithCliente(@Param("id") Integer id);
}
