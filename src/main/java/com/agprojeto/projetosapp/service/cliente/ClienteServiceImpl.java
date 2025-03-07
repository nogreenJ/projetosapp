package com.agprojeto.projetosapp.service.cliente;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agprojeto.projetosapp.model.Cliente;
import com.agprojeto.projetosapp.repository.ClienteRepository;
import com.agprojeto.projetosapp.utils.response.Response;
import com.agprojeto.projetosapp.utils.response.ResponseStatus;

/**
 * Implementação do serviço de cliente, possui implementação básica dos métodos override 
 *  assim como métodos que enforçam a lógica de negócios
 */
@Service
public class ClienteServiceImpl implements ClienteService {

    //Injeta o repository
    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public Cliente saveCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public List<Cliente> listClientes() {
        return (List<Cliente>) clienteRepository.findAll();
    }

    @Override
    public Optional<Cliente> findClienteById(Integer clienteId) {
        return clienteRepository.findById(clienteId);
    }

    @Override
    public Cliente updateCliente(Cliente cliente) {

        Cliente clienteBD = clienteRepository.findById(cliente.getId()).get();
        
        if(clienteBD == null || clienteBD.getId() == null){
            return null;
        }

        //Merge dos dados
        clienteBD.setNome(cliente.getNome());
        clienteBD.setEmail(cliente.getEmail());
        clienteBD.setTelefone(cliente.getTelefone());
        //Atualização de projetos não é pela atualização de clientes
        clienteBD.setProjetoList(null);

        return clienteRepository.save(clienteBD);
    }

    @Override
    public void deleteCliente(Integer clienteId) {
        clienteRepository.deleteById(clienteId);        
    }

    /**
     * Valida cliente sendo criado, então chama método para criá-lo, retornando-o
     * @param cliente Objeto com os dados a serem inseridos
     * @return Resposta contendo o cliente criado, ou o aviso de validação
     */
    public Response validateCreateCliente(Cliente cliente){
        //Validações básicas da entidade
        Response clienteValid = cliente.validate();

        if(clienteValid != null){
            return clienteValid;
        }
        Response response = new Response("Cliente criado com sucesso")
                            .addToDataMap("obj", saveCliente(cliente));

        return response;
    }
    
    /**
     * Valida cliente sendo atualizado, então chama método para realizar o merge de dados, retornando o cliente atualizado
     * @param cliente Objeto com os dados a serem atualizados
     * @return Resposta contendo o cliente atualizado, ou o aviso de validação
     */
    public Response validateUpdateCliente(Cliente cliente) {
        
        //Validações básicas da entidade
        Response clienteValid = cliente.validate();

        if(clienteValid != null){
            return clienteValid;
        }

        if(cliente.getId() == null){
            return new Response(ResponseStatus.WARNING, "Identificador não definido para atualização de cliente");
        }

        Cliente newCliente = updateCliente(cliente);

        if(newCliente == null || newCliente.getId() == null){
            return new Response(ResponseStatus.WARNING, "Cliente não encontrado para atualização");
        }

        return new Response("Cliente atualizado com sucesso")
                            .addToDataMap("obj", newCliente);
    } 
    
}
