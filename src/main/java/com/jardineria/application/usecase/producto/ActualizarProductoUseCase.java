
package com.jardineria.application.usecase.producto;

import com.jardineria.domain.model.Producto;
import com.jardineria.domain.model.repository.ProductoRepository;

public class ActualizarProductoUseCase {

    private final ProductoRepository repository;

    public ActualizarProductoUseCase(ProductoRepository repository) {
        this.repository = repository;
    }

    public void ejecutar(Producto producto) {
        repository.actualizar(producto);
    }
}
