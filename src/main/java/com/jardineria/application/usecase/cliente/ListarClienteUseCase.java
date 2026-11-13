
package com.jardineria.application.usecase.cliente;

import com.jardineria.domain.model.Cliente;
import com.jardineria.domain.model.repository.ClienteRepository;

import java.util.List;

public class ListarClienteUseCase {

    private final ClienteRepository repository;

    public ListarClienteUseCase(ClienteRepository repository) {
        this.repository = repository;
    }

    public List<Cliente> ejecutar() {
        return repository.listarTodos();
    }
}
