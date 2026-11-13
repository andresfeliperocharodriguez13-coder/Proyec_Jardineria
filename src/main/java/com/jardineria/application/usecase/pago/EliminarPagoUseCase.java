package com.jardineria.application.usecase.pago;

import com.jardineria.domain.model.repository.PagoRepository;

public class EliminarPagoUseCase {

    private final PagoRepository repository;

    public EliminarPagoUseCase(PagoRepository repository) {
        this.repository = repository;
    }

    public void ejecutar(int codigoCliente, String idTransaccion) {
        repository.eliminarPorId(codigoCliente, idTransaccion);
    }
}