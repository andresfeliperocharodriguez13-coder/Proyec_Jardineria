
package com.jardineria.application.usecase.producto;

import com.jardineria.domain.model.repository.ProductoRepository;

public class EliminarProductoUseCase {

    private final ProductoRepository repository;

    public EliminarProductoUseCase(ProductoRepository repository) {
        this.repository = repository;
    }

    public void ejecutar(String codigoProducto) {
        repository.eliminarPorId(codigoProducto);
    }
}
