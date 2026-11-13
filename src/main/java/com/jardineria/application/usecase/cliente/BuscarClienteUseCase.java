package com.jardineria.application.usecase.cliente;

import com.jardineria.domain.model.Cliente;
import com.jardineria.domain.model.repository.ClienteRepository;
import java.util.Optional;

public class BuscarClienteUseCase {

    private final ClienteRepository repository;

    public BuscarClienteUseCase(ClienteRepository repository) {
        this.repository = repository;
    }

    public Optional<Cliente> ejecutar(int codigoCliente) {
        return repository.buscarPorId(codigoCliente);
    }
}