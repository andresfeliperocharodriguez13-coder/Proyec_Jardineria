package com.jardineria.application.usecase.pago;

import com.jardineria.domain.model.Pago;
import com.jardineria.domain.model.repository.PagoRepository;

import java.util.List;

public class ListarPagosUseCase {

    private final PagoRepository repository;

    public ListarPagosUseCase(PagoRepository repository) {
        this.repository = repository;
    }

    public List<Pago> ejecutar() {
        return repository.listarTodos();
    }
}