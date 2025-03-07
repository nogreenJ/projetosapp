package com.agprojeto.projetosapp.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.agprojeto.projetosapp.model.enumerations.ProjetoStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
    @Column(columnDefinition = "VARCHAR(16)")
    private ProjetoStatus status = ProjetoStatus.EM_DESENVOLVIMENTO;

    @ManyToOne(targetEntity = Cliente.class, fetch = FetchType.LAZY, optional = false)
    private Cliente clienteParent;

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
}
