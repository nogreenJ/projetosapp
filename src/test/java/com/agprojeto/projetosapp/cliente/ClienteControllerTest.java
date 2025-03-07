package com.agprojeto.projetosapp.cliente;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.agprojeto.projetosapp.controller.ClienteController;
import com.agprojeto.projetosapp.model.Cliente;
import com.agprojeto.projetosapp.service.cliente.ClienteServiceImpl;
import com.agprojeto.projetosapp.utils.response.Response;
import com.agprojeto.projetosapp.utils.response.ResponseStatus;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Testa endpoints do controller de cliente
 */
@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteServiceImpl clienteService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tenta inserir o cliente sem nome, o que não é permitido
     * @throws Exception
     */
    @Test
    public void testaInsertClienteSad() throws Exception {
        Response response = new Response(ResponseStatus.WARNING, "É obrigatório informar o nome do cliente");
        when(clienteService.validateCreateCliente(any(Cliente.class))).thenReturn(response);

        Cliente cliente = new Cliente().setEmail("joao@mail.com").setTelefone("(54) 99999-9999");
    
        mockMvc.perform(
                    MockMvcRequestBuilders.post("/api/cliente")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente))
                ) 
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("WARNING"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.descricao").value("É obrigatório informar o nome do cliente"));
    }

    /**
     * Tenta inserir o cliente normalmente
     * @throws Exception
     */
    @Test
    public void testaInsertClienteHappy() throws Exception {
        Cliente cliente = new Cliente().setNome("Joao Tal").setEmail("joao@mail.com").setTelefone("(54) 99999-9999");

        Response response = new Response(ResponseStatus.SUCCESS, "Cliente criado com sucesso")
                                    .addToDataMap("obj", cliente);
        when(clienteService.validateCreateCliente(any(Cliente.class))).thenReturn(response);
    
        mockMvc.perform(
                    MockMvcRequestBuilders.post("/api/cliente")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente))
                ) 
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.descricao").value("Cliente criado com sucesso"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dataMap.obj.nome").value("Joao Tal"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dataMap.obj.email").value("joao@mail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dataMap.obj.telefone").value("(54) 99999-9999"));
    }

    /**
     * Tenta atualização o cliente sem nome, o que não é permitido
     * @throws Exception
     */
    @Test
    public void testaUpdateClienteSad() throws Exception {
        Response response = new Response(ResponseStatus.WARNING, "É obrigatório informar o nome do cliente");
        when(clienteService.validateUpdateCliente(any(Cliente.class))).thenReturn(response);

        Cliente cliente = new Cliente(1).setEmail("joao@mail.com").setTelefone("(54) 99999-9999");
    
        mockMvc.perform(
                    MockMvcRequestBuilders.put("/api/cliente")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente))
                ) 
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("WARNING"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.descricao").value("É obrigatório informar o nome do cliente"));
    }

    /**
     * Tenta atualização do cliente normalmente
     * @throws Exception
     */
    @Test
    public void testaUpdateClienteHappy() throws Exception {

        Cliente cliente = new Cliente(1).setNome("Joao Tal").setEmail("joao@mail.com").setTelefone("(54) 99999-9999");
        Response response = new Response(ResponseStatus.SUCCESS, "Cliente atualizado com sucesso")
                                .addToDataMap("obj", cliente);

        when(clienteService.validateUpdateCliente(any(Cliente.class))).thenReturn(response);
    
        mockMvc.perform(
                    MockMvcRequestBuilders.put("/api/cliente")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente))
                ) 
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.descricao").value("Cliente atualizado com sucesso"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dataMap.obj.nome").value("Joao Tal"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dataMap.obj.email").value("joao@mail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dataMap.obj.telefone").value("(54) 99999-9999"));
    }

    @Test
    public void testaFindClienteByIdHappy() throws Exception {
        Cliente cliente = new Cliente(1).setNome("Joao Tal").setEmail("joao@mail.com").setTelefone("(54) 99999-9999");
        when(clienteService.findClienteById(1)).thenReturn(Optional.of(cliente));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/cliente/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dataMap.obj.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dataMap.obj.nome").value("Joao Tal"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dataMap.obj.email").value("joao@mail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dataMap.obj.telefone").value("(54) 99999-9999"));
    }

    @Test
    public void testaListClientes() throws Exception {
        List<Cliente> clientes = new ArrayList<>();
        clientes.add(new Cliente(1).setNome("Joao Tal").setEmail("joao@mail.com").setTelefone("(54) 99999-9999"));
        clientes.add(new Cliente(2).setNome("Joana Tal").setEmail("joana@mail.com").setTelefone("(54) 88888-8888"));
        when(clienteService.listClientes()).thenReturn(clientes);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/cliente"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dataMap.list.[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dataMap.list.[0].nome").value("Joao Tal"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dataMap.list.[0].email").value("joao@mail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dataMap.list.[0].telefone").value("(54) 99999-9999"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dataMap.list.[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dataMap.list.[1].nome").value("Joana Tal"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dataMap.list.[1].email").value("joana@mail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dataMap.list.[1].telefone").value("(54) 88888-8888"));
    }
}
