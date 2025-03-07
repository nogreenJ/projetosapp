package com.agprojeto.projetosapp.cliente;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.agprojeto.projetosapp.model.Cliente;
import com.agprojeto.projetosapp.repository.ClienteRepository;
import com.agprojeto.projetosapp.service.cliente.ClienteServiceImpl;

/**
 * Testa services do cliente
 */
public class ClienteServiceTest {

    @InjectMocks
    private ClienteServiceImpl clienteService;

    @Mock
    private ClienteRepository clienteRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindClienteById() {
        Cliente cliente = new Cliente().setNome("Joao Tal").setEmail("joao@mail.com").setTelefone("(54) 99999-9999");

        when(clienteRepository.save(cliente)).thenReturn(cliente);
        when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.of(cliente));

        Cliente savedCliente = clienteRepository.save(cliente);    
        System.out.println("Cliente criado: " + savedCliente);

        Cliente found = clienteService.findClienteById(cliente.getId()).orElse(null);
        assertEquals("Joao Tal", found.getNome());
        assertEquals("joao@mail.com", found.getEmail());
        assertEquals("(54) 99999-9999", found.getTelefone());
    }
}
