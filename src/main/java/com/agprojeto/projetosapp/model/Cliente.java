package com.agprojeto.projetosapp.model;

import java.io.Serializable;
import java.util.List;

import com.agprojeto.projetosapp.utils.response.Response;
import com.agprojeto.projetosapp.utils.response.ResponseStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "clientes")
public class Cliente implements Serializable {

    @Id
    @SequenceGenerator(name = "seq_cliente", sequenceName = "seq_cliente_id", allocationSize = 1)
    @GeneratedValue(generator = "seq_cliente", strategy = GenerationType.SEQUENCE)
    @Column(name = "cliente_id")
    private Integer id;

    @Column(nullable = false)
    private String nome;

    private String email;

    private String telefone;

    @OneToMany(targetEntity = Projeto.class, fetch = FetchType.LAZY, 
                cascade = CascadeType.REMOVE, mappedBy = "clienteParent")
    private List<Projeto> projetoList;

    public Cliente() {
    }

    public Cliente(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public Cliente setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getNome() {
        return nome;
    }

    public Cliente setNome(String nome) {
        this.nome = nome;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Cliente setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getTelefone() {
        return telefone;
    }

    public Cliente setTelefone(String telefone) {
        this.telefone = telefone;
        return this;
    }

    public List<Projeto> getProjetoList() {
        return projetoList;
    }

    public Cliente setProjetoList(List<Projeto> projetoList) {
        this.projetoList = projetoList;
        return this;
    }

    /**
     * Validações básicas da entidade
     * @return Resposta com mensagem de aviso caso não seja válida, ou resposta com status = sucesso caso for
     */
    public Response validate(){
        if(this.nome == null || this.nome.length() == 0){
            return new Response(ResponseStatus.WARNING, "É obrigatório informar o nome do cliente");
        }
        return null;
    }
}
