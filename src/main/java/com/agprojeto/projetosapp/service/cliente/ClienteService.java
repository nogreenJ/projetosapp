package com.agprojeto.projetosapp.service.cliente;

import java.util.List;
import java.util.Optional;

import com.agprojeto.projetosapp.model.Cliente;

/**
 * Serviço que define métodos CRUD para Clientes
 */
public interface ClienteService {
    
    /**
     * Salva um cliente
     * @param cliente Instância de cliente com todas as informações necessárias
     * @return O cliente persistido
     */
    Cliente saveCliente(Cliente cliente);

    /**
     * Listagem de todos os clientes do banco
     * @return Lista de clientes
     */
    List<Cliente> listClientes();

    /**
     * Busca os dados do cliente pelo seu id
     * @param clienteId Identificador do cliente
     * @return O cliente encontrado, null se não encontrou
     */
    Optional<Cliente> findClienteById(Integer clienteId);

    /**
     * Atualiza as informações de um cliente
     * @param cliente Cliente com os novos dados, é obrigatório que tenha o id informado
     * @return Cliente com os dados atualizados
     */
    Cliente updateCliente(Cliente cliente);

    /**
     * Deleta um cliente do banco
     * @param clienteId Identificador do cliente a ser deletado
     */
    void deleteCliente(Integer clienteId);
}
