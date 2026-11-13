package com.jardineria.application.usecase.gamaproducto;

import com.jardineria.domain.model.GamaProducto;
import com.jardineria.domain.model.repository.GamaProductoRepository;

public class ActualizarGamaProductoUseCase {

    private final GamaProductoRepository repository;

    public ActualizarGamaProductoUseCase(GamaProductoRepository repository) {
        this.repository = repository;
    }

    public void ejecutar(GamaProducto gamaProducto) {
        repository.actualizar(gamaProducto);
    }
}