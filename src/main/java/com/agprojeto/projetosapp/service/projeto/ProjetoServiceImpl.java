package com.agprojeto.projetosapp.service.projeto;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agprojeto.projetosapp.model.Projeto;
import com.agprojeto.projetosapp.model.enumerations.ProjetoStatus;
import com.agprojeto.projetosapp.repository.ProjetoRepository;
import com.agprojeto.projetosapp.utils.response.Response;
import com.agprojeto.projetosapp.utils.response.ResponseStatus;

/**
 * Implementação do serviço de projeto, possui implementação básica dos métodos override 
 *  assim como métodos que enforçam a lógica de negócios
 */
@Service
public class ProjetoServiceImpl implements ProjetoService {

    //Injeta o repository
    @Autowired
    private ProjetoRepository projetoRepository;

    @Override
    public Projeto saveProjeto(Projeto projeto) {
        return projetoRepository.save(projeto);
    }

    @Override
    public List<Projeto> listProjetos() {
        return (List<Projeto>) projetoRepository.findAllWithCliente();
    }

    @Override
    public Optional<Projeto> findProjetoById(Integer projetoId) {
        return projetoRepository.findByIdWithCliente(projetoId);
    }

    /**
     * Realiza a deleção de um projeto
     * @param projetoId
     */
    @Override
    public void deleteProjeto(Integer projetoId) {
        projetoRepository.deleteById(projetoId);        
    }

    /**
     * Realiza a finalização de um projeto
     * @param projetoId
     */
    public void finalizarProjeto(Integer projetoId) {
        updateStatusProjeto(projetoId, ProjetoStatus.FINALIZADO);
    }

    /**
     * Realiza o cancelamento de um projeto
     * @param projetoId
     */
    public void cancelarProjeto(Integer projetoId) {
        updateStatusProjeto(projetoId, ProjetoStatus.CANCELADO);
    }

    /**
     * Atualiza apenas o status do projeto, ou data de conclusão se for status concluido (cancelado, finalizado)
     * @param projetoId
     * @param novoStatus
     */
    private void updateStatusProjeto(Integer projetoId, ProjetoStatus novoStatus){
        
        Projeto projetoBD = projetoRepository.findById(projetoId).get(); 
        
        projetoBD.setStatus(novoStatus);
        if(!novoStatus.equals(ProjetoStatus.EM_DESENVOLVIMENTO)){
            projetoBD.setDataConclusao(new Date());  
        }

        projetoRepository.save(projetoBD);
    }

    /**
     * Valida projeto sendo criado, então chama método para criá-lo, retornando-o
     * @param projeto Objeto com os dados a serem inseridos
     * @return Resposta contendo o projeto criado, ou o aviso de validação
     */
    public Response validateCreateProjeto(Projeto projeto){
        //Validações básicas da entidade
        Response projetoValid = projeto.validate();

        if(projetoValid != null){
            return projetoValid;
        }
        Response response = new Response("Projeto criado com sucesso")
                            .addToDataMap("obj", saveProjeto(projeto));

        return response;
    }
    
    /**
     * Valida projeto sendo atualizado, então chama método para realizar o merge de dados, retornando o projeto atualizado
     * @param projeto Objeto com os dados a serem atualizados
     * @return Resposta contendo o projeto atualizado, ou o aviso de validação
     */
    public Response validateUpdateProjeto(Projeto projeto) {
        
        if(projeto.getId() == null){
            return new Response(ResponseStatus.WARNING, "Identificador não definido para atualização de projeto");
        }

        //Validações básicas da entidade
        Response projetoValid = projeto.validate();

        if(projetoValid != null){
            return projetoValid;
        }

        Projeto newProjeto = updateProjeto(projeto);

        if(newProjeto == null || newProjeto.getId() == null){
            return new Response(ResponseStatus.WARNING, "Projeto não encontrado para atualização");
        }

        return new Response("Projeto atualizado com sucesso")
                            .addToDataMap("obj", newProjeto);
    } 

    @Override
    public Projeto updateProjeto(Projeto projeto) {

        Projeto projetoBD = projetoRepository.findById(projeto.getId()).get();
        
        if(projetoBD == null || projetoBD.getId() == null){
            return null;
        }

        //Merge dos dados
        projetoBD.setTitulo(projeto.getTitulo());
        projetoBD.setStatus(projeto.getStatus());
        projetoBD.setDataPrevista(projeto.getDataPrevista());
        projetoBD.setDescricao(projeto.getDescricao());
        projetoBD.setAtividadeList(projeto.getAtividadeList());

        return projetoRepository.save(projetoBD);
    }
    
}
