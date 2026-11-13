package com.jardineria.application.usecase.gamaproducto;

import com.jardineria.domain.model.repository.GamaProductoRepository;

public class EliminarGamaProductoUseCase {

    private final GamaProductoRepository repository;

    public EliminarGamaProductoUseCase(GamaProductoRepository repository) {
        this.repository = repository;
    }

    public void ejecutar(String gama) {
        repository.eliminarPorId(gama);
    }
}
