package com.agprojeto.projetosapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agprojeto.projetosapp.model.Cliente;

/**
 * Interface de repository para a classe Cliente, provicência métodos CRUD por meio do JpaRepository
 */
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {}
