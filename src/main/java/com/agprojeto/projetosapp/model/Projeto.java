package com.agprojeto.projetosapp.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.agprojeto.projetosapp.model.enumerations.ProjetoStatus;
import com.agprojeto.projetosapp.utils.response.Response;
import com.agprojeto.projetosapp.utils.response.ResponseStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "projetos")
public class Projeto implements Serializable {
    
    @Id
    @SequenceGenerator(name = "seq_projeto", sequenceName = "seq_projeto_id", allocationSize = 1)
    @GeneratedValue(generator = "seq_projeto", strategy = GenerationType.SEQUENCE)
    @Column(name = "projeto_id")
    private Integer id;

    @Column(nullable = false)
    private String titulo;
    
    private String descricao;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao = new Date();
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataPrevista;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataConclusao;

    @Enumerated(EnumType.STRING)
    private ProjetoStatus status = ProjetoStatus.EM_DESENVOLVIMENTO;

    @JoinColumn(name="CLIENTE_ID")
    @ManyToOne(targetEntity = Cliente.class, fetch = FetchType.LAZY, optional = false)
    private Cliente clienteParent;

    @OneToMany(mappedBy = "projetoParent", fetch = FetchType.LAZY)
    @JsonManagedReference("projetoAtividades")
    private List<Atividade> atividadeList;

    public Projeto() {
    }

    public Projeto(Integer id) {
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

    public Date getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(Date dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    public ProjetoStatus getStatus() {
        return status;
    }

    public void setStatus(ProjetoStatus status) {
        this.status = status;
    }

    public Cliente getClienteParent() {
        return clienteParent;
    }

    public void setClienteParent(Cliente clienteParent) {
        this.clienteParent = clienteParent;
    }

    public List<Atividade> getAtividadeList() {
        return atividadeList;
    }

    public void setAtividadeList(List<Atividade> atividadeList) {
        this.atividadeList = atividadeList;
    }

    /**
     * Validações básicas da entidade
     * @return Resposta com mensagem de aviso caso não seja válida, ou resposta com status = sucesso caso for
     */
    public Response validate(){
        if(this.titulo == null || this.titulo.length() == 0){
            return new Response(ResponseStatus.WARNING, "É obrigatório informar o título do Projeto");
        }
        if(this.descricao == null || this.descricao.length() == 0){
            return new Response(ResponseStatus.WARNING, "É obrigatório informar a descrição do Projeto");
        }
        if(this.dataPrevista == null){
            return new Response(ResponseStatus.WARNING, "É obrigatório informar a data prevista do Projeto");
        }
        if(this.clienteParent == null || this.clienteParent.getId() == null){
            return new Response(ResponseStatus.WARNING, "É obrigatório informar o cliente do Projeto");
        }
        return null;
    }
}
