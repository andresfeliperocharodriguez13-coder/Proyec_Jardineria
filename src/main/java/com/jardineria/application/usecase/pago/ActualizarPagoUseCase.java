package com.jardineria.application.usecase.pago;

import com.jardineria.domain.model.Pago;
import com.jardineria.domain.model.repository.PagoRepository;

public class ActualizarPagoUseCase {

    private final PagoRepository repository;

    public ActualizarPagoUseCase(PagoRepository repository) {
        this.repository = repository;
    }

    public void ejecutar(Pago pago) {
        repository.actualizar(pago);
    }
}