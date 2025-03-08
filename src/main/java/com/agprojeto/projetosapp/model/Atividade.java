package com.agprojeto.projetosapp.model;

import java.io.Serializable;
import java.util.Date;

import com.agprojeto.projetosapp.model.enumerations.AtividadeStatus;
import com.agprojeto.projetosapp.utils.response.Response;
import com.agprojeto.projetosapp.utils.response.ResponseStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "atividades")
public class Atividade implements Serializable {
    
    @Id
    @SequenceGenerator(name = "seq_atividade", sequenceName = "seq_atividade_id", allocationSize = 1)
    @GeneratedValue(generator = "seq_atividade", strategy = GenerationType.SEQUENCE)
    @Column(name = "atividade_id")
    private Integer id;

    @Column(nullable = false)
    private String titulo;
    
    private String descricao;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao = new Date();
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataPrevista;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataInicio;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataConclusao;

    @Enumerated(EnumType.STRING)
    private AtividadeStatus status = AtividadeStatus.NOVO;

    @JoinColumn(name = "projeto_id")
    @JsonBackReference("projetoAtividades")
    @ManyToOne(targetEntity = Projeto.class, fetch = FetchType.LAZY, optional = false)
    private Projeto projetoParent;

    public Atividade() {
    }

    public Atividade(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Date getDataPrevista() {
        return dataPrevista;
    }

    public void setDataPrevista(Date dataPrevista) {
        this.dataPrevista = dataPrevista;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(Date dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    public AtividadeStatus getStatus() {
        return status;
    }

    public void setStatus(AtividadeStatus status) {
        this.status = status;
    }

    public Projeto getProjetoParent() {
        return projetoParent;
    }

    public Atividade setProjetoParent(Projeto projetoParent) {
        this.projetoParent = projetoParent;
        return this;
    }

    /**
     * Validações básicas da entidade
     * @return Resposta com mensagem de aviso caso não seja válida, ou resposta com status = sucesso caso for
     */
    public Response validate(){
        if(this.titulo == null || this.titulo.length() == 0){
            return new Response(ResponseStatus.WARNING, "É obrigatório informar o título da Atividade");
        }
        if(this.descricao == null || this.descricao.length() == 0){
            return new Response(ResponseStatus.WARNING, "É obrigatório informar a descrição da Atividade");
        }
        if(this.dataPrevista == null){
            return new Response(ResponseStatus.WARNING, "É obrigatório informar a data prevista do Projeto");
        }
        if(this.projetoParent == null){
            return new Response(ResponseStatus.WARNING, "É obrigatório informar o Projeto");
        }
        return null;
    }
}
