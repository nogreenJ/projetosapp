package com.agprojeto.projetosapp.service.atividade;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agprojeto.projetosapp.model.Atividade;
import com.agprojeto.projetosapp.model.enumerations.AtividadeStatus;
import com.agprojeto.projetosapp.repository.AtividadeRepository;
import com.agprojeto.projetosapp.utils.response.Response;
import com.agprojeto.projetosapp.utils.response.ResponseStatus;

/**
 * Implementação do serviço de atividade, possui implementação básica dos métodos override 
 *  assim como métodos que enforçam a lógica de negócios
 */
@Service
public class AtividadeServiceImpl implements AtividadeService {

    //Injeta o repository
    @Autowired
    private AtividadeRepository atividadeRepository;

    @Override
    public Atividade saveAtividade(Atividade atividade) {
        return atividadeRepository.save(atividade);
    }

    @Override
    public List<Atividade> listAtividades() {
        return (List<Atividade>) atividadeRepository.findAll();
    }

    @Override
    public Optional<Atividade> findAtividadeById(Integer atividadeId) {
        return atividadeRepository.findById(atividadeId);
    }

    /**
     * Realiza a deleção de um atividade
     * @param atividadeId
     */
    @Override
    public void deleteAtividade(Integer atividadeId) {
        atividadeRepository.deleteById(atividadeId);        
    }

    /**
     * Realiza a finalização de um atividade
     * @param atividadeId
     */
    public Atividade finalizarAtividade(Integer atividadeId) {
        return updateStatusAtividade(atividadeId, AtividadeStatus.FINALIZADO);
    }

    /**
     * Realiza o cancelamento de um atividade
     * @param atividadeId
     */
    public Atividade cancelarAtividade(Integer atividadeId) {
        return updateStatusAtividade(atividadeId, AtividadeStatus.CANCELADO);
    }

    /**
     * Inicia de um atividade
     * @param atividadeId
     */
    public Atividade iniciarAtividade(Integer atividadeId) {
        return updateStatusAtividade(atividadeId, AtividadeStatus.EM_DESENVOLVIMENTO);
    }

    /**
     * Atualiza apenas o status do atividade, ou data de conclusão se for status concluido (cancelado, finalizado)
     * @param atividadeId
     * @param novoStatus
     */
    private Atividade updateStatusAtividade(Integer atividadeId, AtividadeStatus novoStatus){
        
        Atividade atividadeBD = atividadeRepository.findById(atividadeId).get(); 
        
        atividadeBD.setStatus(novoStatus);
        if(novoStatus.equals(AtividadeStatus.CANCELADO) || novoStatus.equals(AtividadeStatus.FINALIZADO)){
            atividadeBD.setDataConclusao(new Date());  
        }
        if(novoStatus.equals(AtividadeStatus.EM_DESENVOLVIMENTO)){
            atividadeBD.setDataInicio(new Date());  
        }

        return atividadeRepository.save(atividadeBD);
    }

    /**
     * Valida atividade sendo criado, então chama método para criá-lo, retornando-o
     * @param atividade Objeto com os dados a serem inseridos
     * @return Resposta contendo o atividade criado, ou o aviso de validação
     */
    public Response validateCreateAtividade(Atividade atividade){
        //Validações básicas da entidade
        Response atividadeValid = atividade.validate();

        if(atividadeValid != null){
            return atividadeValid;
        }
        atividade.setDataCriacao(new Date());
        Response response = new Response("Atividade criada com sucesso")
                            .addToDataMap("obj", saveAtividade(atividade));

        return response;
    }
    
    /**
     * Valida atividade sendo atualizado, então chama método para realizar o merge de dados, retornando o atividade atualizado
     * @param atividade Objeto com os dados a serem atualizados
     * @return Resposta contendo o atividade atualizado, ou o aviso de validação
     */
    public Response validateUpdateAtividade(Atividade atividade) {
        
        if(atividade.getId() == null){
            return new Response(ResponseStatus.WARNING, "Identificador não definido para atualização de atividade");
        }

        //Validações básicas da entidade
        Response atividadeValid = atividade.validate();

        if(atividadeValid != null){
            return atividadeValid;
        }

        Atividade newAtividade = updateAtividade(atividade);

        if(newAtividade == null || newAtividade.getId() == null){
            return new Response(ResponseStatus.WARNING, "Atividade não encontrada para atualização");
        }

        return new Response("Atividade atualizada com sucesso")
                            .addToDataMap("obj", newAtividade);
    } 

    @Override
    public Atividade updateAtividade(Atividade atividade) {

        Atividade atividadeBD = atividadeRepository.findById(atividade.getId()).get();
        
        if(atividadeBD == null || atividadeBD.getId() == null){
            return null;
        }

        //Merge dos dados
        atividadeBD.setTitulo(atividade.getTitulo());
        atividadeBD.setStatus(atividade.getStatus());
        atividadeBD.setDataPrevista(atividade.getDataPrevista());
        atividadeBD.setDescricao(atividade.getDescricao());

        return atividadeRepository.save(atividadeBD);
    }
    
}
