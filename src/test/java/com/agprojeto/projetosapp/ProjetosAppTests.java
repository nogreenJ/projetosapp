package com.agprojeto.projetosapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EntityScan(basePackages = "com.agprojeto.projetosapp.model")
public class ProjetosAppTests {
    @Test
    void contextLoads() {
    }
}
