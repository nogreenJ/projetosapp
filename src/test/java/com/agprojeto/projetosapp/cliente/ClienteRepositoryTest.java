package com.agprojeto.projetosapp.cliente;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.agprojeto.projetosapp.model.Cliente;
import com.agprojeto.projetosapp.repository.ClienteRepository;

/**
 * Testa as operações do repositório de clientes
 */
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ClienteRepositoryTest {

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    public void testSave() {
        Cliente cliente = new Cliente().setNome("Joao Tal").setEmail("joao@mail.com").setTelefone("(54) 99999-9999");
        Cliente savedCliente = clienteRepository.save(cliente);
        assertNotNull(savedCliente);
        assertEquals("Joao Tal", savedCliente.getNome());
        clienteRepository.deleteById(savedCliente.getId());
    }

    @Test
    public void testGet() {
        Cliente cliente = clienteRepository.save(new Cliente().setNome("Joao Tal").setEmail("joao@mail.com").setTelefone("(54) 99999-9999"));
        Cliente fetchedCliente = clienteRepository.findById(cliente.getId()).orElse(null);
        assertNotNull(fetchedCliente);
        assertEquals(cliente.getId(), fetchedCliente.getId());
        clienteRepository.deleteById(fetchedCliente.getId());
    }

    @Test
    public void testList() {
        Cliente cli1 = clienteRepository.save(new Cliente().setNome("Joao Tal").setEmail("joao@mail.com").setTelefone("(54) 99999-9999"));
        Cliente cli2 = clienteRepository.save(new Cliente().setNome("Joana Tal").setEmail("joana@mail.com").setTelefone("(54) 88888-8888"));
        List<Cliente> clientes = clienteRepository.findAll();
        assertNotNull(clientes);
        assertEquals(2, clientes.size());
        clienteRepository.deleteById(cli1.getId());
        clienteRepository.deleteById(cli2.getId());
    }

    @Test
    public void testUpdate() {
        Cliente cliente = clienteRepository.save(new Cliente().setNome("Joao Tal").setEmail("joao@mail.com").setTelefone("(54) 99999-9999"));
        cliente.setNome("Joao Taletal");
        cliente.setEmail("joao.taletal@mail.com");
        cliente.setTelefone("(54) 77777-7777");
        clienteRepository.save(cliente);
        Cliente updatedCliente = clienteRepository.findById(cliente.getId()).orElse(null);
        assertNotNull(updatedCliente);
        assertEquals("Joao Taletal", updatedCliente.getNome());
        assertEquals("joao.taletal@mail.com", updatedCliente.getEmail());
        assertEquals("(54) 77777-7777", updatedCliente.getTelefone());
        clienteRepository.deleteById(updatedCliente.getId());
    }

    @Test
    public void testDeleteCliente() {
        Cliente cliente = clienteRepository.save(new Cliente().setNome("Joao Tal").setEmail("joao@mail.com").setTelefone("(54) 99999-9999"));
        clienteRepository.deleteById(cliente.getId());
        Cliente deletedCliente = clienteRepository.findById(cliente.getId()).orElse(null);
        assertNull(deletedCliente);
    }
}