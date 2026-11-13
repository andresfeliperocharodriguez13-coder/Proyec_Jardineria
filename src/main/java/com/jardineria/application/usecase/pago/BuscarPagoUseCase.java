package com.jardineria.application.usecase.pago;

import com.jardineria.domain.model.Pago;
import com.jardineria.domain.model.repository.PagoRepository;

import java.util.Optional;

public class BuscarPagoUseCase {

    private final PagoRepository repository;

    public BuscarPagoUseCase(PagoRepository repository) {
        this.repository = repository;
    }

    public Optional<Pago> ejecutar(
            int codigoCliente,
            String idTransaccion) {

        return repository.buscarPorId(codigoCliente, idTransaccion);
    }
}