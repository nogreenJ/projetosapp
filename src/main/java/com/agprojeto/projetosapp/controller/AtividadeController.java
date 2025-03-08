package com.agprojeto.projetosapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agprojeto.projetosapp.model.Atividade;
import com.agprojeto.projetosapp.service.atividade.AtividadeServiceImpl;
import com.agprojeto.projetosapp.utils.response.Response;
import com.agprojeto.projetosapp.utils.response.ResponseStatus;

/**
 * Controller das operações relacionadas ao CRUD de Atividades
 */
@RestController
@RequestMapping("/api/atividade")
public class AtividadeController extends ControllerSuperclass {
    
    @Autowired
    private AtividadeServiceImpl atividadeService;

    /**
     * Endpoint de criação do atividade
     * @param atividade atividade a ser criado
     * @return Resposta carregando o atividade criado, ou resposta avisando por que não criou
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response saveAtividade(@RequestBody Atividade atividade) {
        try {
            return atividadeService.validateCreateAtividade(atividade);
        } catch (Exception e) {
            return errorResponse(e, "Erro ao inserir Atividade");
        }
    }

    /**
     * Busca listagem de atividades cadastrados
     * @return Uma resposta carregando a listagem de atividades
     */
    @GetMapping
    public Response fetchAtividadeList() {
        try {
            return new Response()
                        .addToDataMap("list", atividadeService.listAtividades());
        } catch (Exception e) {
            return errorResponse(e, "Erro ao listar Atividades");
        }
    }

    /**
     * Busca um atividade específico pelo seu ID
     * @param atividadeId O identificador do atividade a ser buscado
     * @return Uma resposta com o atividade encontrado, avisando se não foi encontrado
     */
    @GetMapping("/{id}")
    public Response getAtividadeById(@PathVariable("id") Integer atividadeId) {
        try {
            Atividade atividade = atividadeService.findAtividadeById(atividadeId).orElse(null);
            if(atividade != null && atividade.getId() != null){
                return new Response()
                            .addToDataMap("obj", atividade);
            } 
            return new Response(ResponseStatus.WARNING, "Não foi encontrada um atividade com id " + atividadeId);
        } catch (Exception e) {
            return errorResponse(e, "Erro ao buscar Atividade");
        }
    }

    /**
     * Atualiza os dados de um atividade
     * @param atividade O atividade com os novos dados
     * @return Uma resposta com o atividade após atualização
     */
    @PutMapping
    public Response updateAtividade(@RequestBody Atividade atividade) {
        try {
            return atividadeService.validateUpdateAtividade(atividade);
        } catch (Exception e) {
            return errorResponse(e, "Erro ao atualizar Atividade");
        }
    }

    /**
     * Realiza a deleção de um atividade
     * @param atividadeId O identificador da atividade a ser deletada
     * @return Uma resposta indicando se houve sucesso
     */
    @DeleteMapping("{id}")
    public Response deleteAtividadeById(@PathVariable("id") Integer atividadeId) {
        try{
            atividadeService.deleteAtividade(atividadeId);
            return new Response("Atividade removida com sucesso");
        } catch (Exception e){
            return errorResponse(e, "Erro ao tentar deletar atividade " + atividadeId);
        }
    }

    /**
     * Realiza a finalização de uma atividade
     * @param atividadeId O identificador da atividade a ser finalizada
     * @return Uma resposta indicando se houve sucesso
     */
    @PutMapping("/finalizar/{id}")
    public Response finalizarAtividadeById(@PathVariable("id") Integer atividadeId) {
        try{
            return new Response("Atividade finalizada com sucesso")
                        .addToDataMap("obj", atividadeService.finalizarAtividade(atividadeId));
        } catch (Exception e){
            return errorResponse(e, "Erro ao tentar finalizar atividade " + atividadeId);
        }
    }

    /**
     * Realiza a cancelamento de uma atividade
     * @param atividadeId O identificador da atividade a ser cancelada
     * @return Uma resposta indicando se houve sucesso
     */
    @PutMapping("/cancelar/{id}")
    public Response cancelarAtividadeById(@PathVariable("id") Integer atividadeId) {
        try{
            return new Response("Atividade cancelada com sucesso")
                        .addToDataMap("obj", atividadeService.cancelarAtividade(atividadeId));
        } catch (Exception e){
            return errorResponse(e, "Erro ao tentar cancelar atividade " + atividadeId);
        }
    }

    /**
     * Inicia uma atividade
     * @param atividadeId O identificador da atividade a ser iniciada
     * @return Uma resposta indicando se houve sucesso
     */
    @PutMapping("/iniciar/{id}")
    public Response iniciarAtividadeById(@PathVariable("id") Integer atividadeId) {
        try{
            return new Response("Atividade iniciada com sucesso")
                        .addToDataMap("obj", atividadeService.iniciarAtividade(atividadeId));
        } catch (Exception e){
            return errorResponse(e, "Erro ao tentar iniciar atividade " + atividadeId);
        }
    }
}
