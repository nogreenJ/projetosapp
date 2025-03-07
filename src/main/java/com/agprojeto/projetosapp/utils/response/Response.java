package com.agprojeto.projetosapp.utils.response;

import java.util.HashMap;

public class Response {
    
    private String descricao = "";
    private ResponseStatus status = ResponseStatus.SUCCESS;
    private HashMap<String, Object> dataMap = new HashMap<>();

    public Response() {
    }

    public Response(ResponseStatus status) {
        this.status = status;
    }

    public Response(String descricao) {
        this.descricao = descricao;
    }

    public Response(ResponseStatus status, String descricao) {
        this.status = status;
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public Response setDescricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public Response setStatus(ResponseStatus status) {
        this.status = status;
        return this;
    }

    public HashMap<String, Object> getDataMap() {
        return dataMap;
    }

    public Response setDataMap(HashMap<String, Object> dataMap) {
        this.dataMap = dataMap;
        return this;
    }    

    public Response addToDataMap(String desc, Object obj){
        if(this.dataMap == null){
            this.dataMap = new HashMap<>();
        }
        this.dataMap.put(desc, obj);
        return this;
    }
}
