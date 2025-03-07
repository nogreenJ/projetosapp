package com.agprojeto.projetosapp.controller;

import com.agprojeto.projetosapp.utils.response.Response;
import com.agprojeto.projetosapp.utils.response.ResponseStatus;

/**
 * Superclasse para operações básicas dos controllers
 */
public class ControllerSuperclass {
    
    /**
     * Resposta de erro padrão sem mensagem
     * @param e Exceção ocorrida
     * @return Resposta de erro
     */
    protected Response errorResponse(Exception e){
        return errorResponse(e, null);
    }
    
    /**
     * Resposta de erro padrão com mensagem
     * @param e Exceção ocorrida
     * @param msg Mensagem explicativa
     * @return Resposta de erro
     */
    protected Response errorResponse(Exception e, String msg){
        System.out.println(msg != null ? msg + " - " + e : e);
        return new Response(ResponseStatus.ERROR, msg);
    }
}
