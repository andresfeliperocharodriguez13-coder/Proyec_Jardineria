package com.jardineria.application.usecase.gamaproducto;

import com.jardineria.domain.model.GamaProducto;
import com.jardineria.domain.model.repository.GamaProductoRepository;

public class GuardarGamaProductoUseCase {

    private final GamaProductoRepository repository;

    public GuardarGamaProductoUseCase(GamaProductoRepository repository) {
        this.repository = repository;
    }

    public void ejecutar(GamaProducto gamaProducto) {
        repository.guardar(gamaProducto);
    }
}
