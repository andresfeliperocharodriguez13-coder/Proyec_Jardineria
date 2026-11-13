
package com.jardineria.application.usecase.producto;

import com.jardineria.domain.model.Producto;
import com.jardineria.domain.model.repository.ProductoRepository;

import java.util.List;

public class ListarProductosUseCase {

    private final ProductoRepository repository;

    public ListarProductosUseCase(ProductoRepository repository) {
        this.repository = repository;
    }

    public List<Producto> ejecutar() {
        return repository.listarTodos();
    }
}
