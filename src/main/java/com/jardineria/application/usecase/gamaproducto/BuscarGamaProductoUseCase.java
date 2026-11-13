package com.jardineria.application.usecase.gamaproducto;

import com.jardineria.domain.model.GamaProducto;
import com.jardineria.domain.model.repository.GamaProductoRepository;
import java.util.Optional;

public class BuscarGamaProductoUseCase {

    private final GamaProductoRepository repository;

    public BuscarGamaProductoUseCase(GamaProductoRepository repository) {
        this.repository = repository;
    }

    public Optional<GamaProducto> ejecutar(String gama) {
        return repository.buscarPorId(gama);
    }
}
