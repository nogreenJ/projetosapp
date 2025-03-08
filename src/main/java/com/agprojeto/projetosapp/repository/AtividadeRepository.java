package com.agprojeto.projetosapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agprojeto.projetosapp.model.Atividade;

/**
 * Interface de repository para a classe Atividade, providência métodos CRUD por meio do JpaRepository
 */
public interface AtividadeRepository extends JpaRepository<Atividade, Integer>  {
}
