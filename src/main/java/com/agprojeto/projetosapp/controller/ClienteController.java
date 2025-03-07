package com.agprojeto.projetosapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agprojeto.projetosapp.model.Cliente;
import com.agprojeto.projetosapp.service.cliente.ClienteServiceImpl;
import com.agprojeto.projetosapp.utils.response.Response;
import com.agprojeto.projetosapp.utils.response.ResponseStatus;

/**
 * Controller das operações relacionadas ao CRUD de clientes
 */
@RestController
@RequestMapping("/api/cliente")
public class ClienteController extends ControllerSuperclass {
    
    @Autowired
    private ClienteServiceImpl clienteService;

    /**
     * Endpoint de criação do cliente
     * @param cliente cliente a ser criado
     * @return Resposta carregando o cliente criado, ou resposta avisando por que não criou
     */
    @PostMapping
    public Response saveCliente(@RequestBody Cliente cliente) {
        try {
            return clienteService.validateCreateCliente(cliente);
        } catch (Exception e) {
            return errorResponse(e, "Erro ao inserir Cliente");
        }
    }

    /**
     * Busca listagem de clientes cadastrados
     * @return Uma resposta carregando a listagem de clientes
     */
    @GetMapping
    public Response fetchClienteList() {
        try {
            return new Response()
                        .addToDataMap("list", clienteService.listClientes());
        } catch (Exception e) {
            return errorResponse(e, "Erro ao listar Clientes");
        }
    }

    /**
     * Busca um cliente específico pelo seu ID
     * @param clienteId O identificador do cliente a ser buscado
     * @return Uma resposta com o cliente encontrado, avisando se não foi encontrado
     */
    @GetMapping("/{id}")
    public Response getClienteById(@PathVariable("id") Integer clienteId) {
        try {
            Cliente cliente = clienteService.findClienteById(clienteId).orElse(null);
            if(cliente != null && cliente.getId() != null){
                return new Response()
                            .addToDataMap("obj", cliente);
            } 
            return new Response(ResponseStatus.WARNING, "Não foi encontrado um cliente com id " + clienteId);
        } catch (Exception e) {
            return errorResponse(e, "Erro ao buscar Cliente");
        }
    }

    /**
     * Atualiza os dados de um cliente
     * @param cliente O cliente com os novos dados
     * @return Uma resposta com o cliente após atualização
     */
    @PutMapping
    public Response updateCliente(@RequestBody Cliente cliente) {
        try {
            return clienteService.validateUpdateCliente(cliente);
        } catch (Exception e) {
            return errorResponse(e, "Erro ao atualizar Cliente");
        }
    }

    /**
     * Realiza a deleção de um cliente
     * @param clienteId O identificador do cliente a ser deletado
     * @return Uma resposta indicando se houve sucesso
     */
    @DeleteMapping("{id}")
    public Response deleteClienteById(@PathVariable("id") Integer clienteId) {
        try{
            clienteService.deleteCliente(clienteId);
            return new Response("Cliente removido com sucesso");
        } catch (Exception e){
            return errorResponse(e, "Erro ao tentar deletar cliente " + clienteId);
        }
    }
}
