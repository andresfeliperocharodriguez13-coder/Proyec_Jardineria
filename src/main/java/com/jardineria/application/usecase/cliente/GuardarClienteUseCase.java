package com.jardineria.application.usecase.cliente;

import com.jardineria.domain.model.Cliente;
import com.jardineria.domain.model.repository.ClienteRepository;

public class GuardarClienteUseCase {

    private final ClienteRepository repository;

    public GuardarClienteUseCase(ClienteRepository repository) {
        this.repository = repository;
    }

    public void ejecutar(Cliente cliente) {
        repository.guardar(cliente);
    }
}
