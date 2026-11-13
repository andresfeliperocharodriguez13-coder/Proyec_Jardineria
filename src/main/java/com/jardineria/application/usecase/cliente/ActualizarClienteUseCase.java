package com.jardineria.application.usecase.cliente;

import com.jardineria.domain.model.Cliente;
import com.jardineria.domain.model.repository.ClienteRepository;

public class ActualizarClienteUseCase {

    private final ClienteRepository repository;

    public ActualizarClienteUseCase(ClienteRepository repository) {
        this.repository = repository;
    }

    public void ejecutar(Cliente cliente) {
        repository.actualizar(cliente);
    }
}