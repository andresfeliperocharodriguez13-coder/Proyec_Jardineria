package com.jardineria.application.usecase.pago;

import com.jardineria.domain.model.Pago;
import com.jardineria.domain.model.repository.PagoRepository;

public class GuardarPagoUseCase {

    private final PagoRepository repository;

    public GuardarPagoUseCase(PagoRepository repository) {
        this.repository = repository;
    }

    public void ejecutar(Pago pago) {
        repository.guardar(pago);
    }
}