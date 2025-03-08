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

import com.agprojeto.projetosapp.model.Projeto;
import com.agprojeto.projetosapp.service.projeto.ProjetoServiceImpl;
import com.agprojeto.projetosapp.utils.response.Response;
import com.agprojeto.projetosapp.utils.response.ResponseStatus;

/**
 * Controller das operações relacionadas ao CRUD de Projetos
 */
@RestController
@RequestMapping("/api/projeto")
public class ProjetoController extends ControllerSuperclass {
    
    @Autowired
    private ProjetoServiceImpl projetoService;

    /**
     * Endpoint de criação do projeto
     * @param projeto projeto a ser criado
     * @return Resposta carregando o projeto criado, ou resposta avisando por que não criou
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response saveProjeto(@RequestBody Projeto projeto) {
        try {
            return projetoService.validateCreateProjeto(projeto);
        } catch (Exception e) {
            return errorResponse(e, "Erro ao inserir Projeto");
        }
    }

    /**
     * Busca listagem de projetos cadastrados
     * @return Uma resposta carregando a listagem de projetos
     */
    @GetMapping
    public Response fetchProjetoList() {
        try {
            return new Response()
                        .addToDataMap("list", projetoService.listProjetos());
        } catch (Exception e) {
            return errorResponse(e, "Erro ao listar Projetos");
        }
    }

    /**
     * Busca um projeto específico pelo seu ID
     * @param projetoId O identificador do projeto a ser buscado
     * @return Uma resposta com o projeto encontrado, avisando se não foi encontrado
     */
    @GetMapping("/{id}")
    public Response getProjetoById(@PathVariable("id") Integer projetoId) {
        try {
            Projeto projeto = projetoService.findProjetoById(projetoId).orElse(null);
            if(projeto != null && projeto.getId() != null){
                return new Response()
                            .addToDataMap("obj", projeto);
            } 
            return new Response(ResponseStatus.WARNING, "Não foi encontrado um projeto com id " + projetoId);
        } catch (Exception e) {
            return errorResponse(e, "Erro ao buscar Projeto");
        }
    }

    /**
     * Atualiza os dados de um projeto
     * @param projeto O projeto com os novos dados
     * @return Uma resposta com o projeto após atualização
     */
    @PutMapping
    public Response updateProjeto(@RequestBody Projeto projeto) {
        try {
            return projetoService.validateUpdateProjeto(projeto);
        } catch (Exception e) {
            return errorResponse(e, "Erro ao atualizar Projeto");
        }
    }

    /**
     * Realiza a deleção de um projeto
     * @param projetoId O identificador do projeto a ser deletado
     * @return Uma resposta indicando se houve sucesso
     */
    @DeleteMapping("{id}")
    public Response deleteProjetoById(@PathVariable("id") Integer projetoId) {
        try{
            projetoService.deleteProjeto(projetoId);
            return new Response("Projeto removido com sucesso");
        } catch (Exception e){
            return errorResponse(e, "Erro ao tentar deletar projeto " + projetoId);
        }
    }

    /**
     * Realiza a finalização de um projeto
     * @param projetoId O identificador do projeto a ser finalizado
     * @return Uma resposta indicando se houve sucesso
     */
    @PutMapping("/finalizar/{id}")
    public Response finalizarProjetoById(@PathVariable("id") Integer projetoId) {
        try{
            projetoService.finalizarProjeto(projetoId);
            return new Response("Projeto finalizado com sucesso");
        } catch (Exception e){
            return errorResponse(e, "Erro ao tentar finalizar projeto " + projetoId);
        }
    }

    /**
     * Realiza a cancelamento de um projeto
     * @param projetoId O identificador do projeto a ser cancelado
     * @return Uma resposta indicando se houve sucesso
     */
    @PutMapping("/cancelar/{id}")
    public Response cancelarProjetoById(@PathVariable("id") Integer projetoId) {
        try{
            projetoService.cancelarProjeto(projetoId);
            return new Response("Projeto cancelado com sucesso");
        } catch (Exception e){
            return errorResponse(e, "Erro ao tentar cancelar projeto " + projetoId);
        }
    }
}
