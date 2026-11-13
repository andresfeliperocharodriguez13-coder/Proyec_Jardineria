
package com.jardineria.application.usecase.producto;

import com.jardineria.domain.model.Producto;
import com.jardineria.domain.model.repository.ProductoRepository;

public class GuardarProductoUseCase {

    private final ProductoRepository repository;

    public GuardarProductoUseCase(ProductoRepository repository) {
        this.repository = repository;
    }

    public void ejecutar(Producto producto) {
        repository.guardar(producto);
    }
}
