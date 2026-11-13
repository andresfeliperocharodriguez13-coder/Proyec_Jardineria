package com.jardineria.application.usecase.gamaproducto;

import com.jardineria.domain.model.GamaProducto;
import com.jardineria.domain.model.repository.GamaProductoRepository;
import java.util.List;

public class ListarGamasProductoUseCase {

    private final GamaProductoRepository repository;

    public ListarGamasProductoUseCase(GamaProductoRepository repository) {
        this.repository = repository;
    }

    public List<GamaProducto> ejecutar() {
        return repository.listarTodos();
    }
}
