package com.jardineria.application.usecase.cliente;

import com.jardineria.domain.model.repository.ClienteRepository;

public class EliminarClienteUseCase {

    private final ClienteRepository repository;

    public EliminarClienteUseCase(ClienteRepository repository) {
        this.repository = repository;
    }

    public void ejecutar(int codigoCliente) {
        repository.eliminarPorId(codigoCliente);
    }
}