
package com.jardineria.application.usecase.producto;

import com.jardineria.domain.model.Producto;
import com.jardineria.domain.model.repository.ProductoRepository;

import java.util.Optional;

public class BuscarProductoUseCase {

    private final ProductoRepository repository;

    public BuscarProductoUseCase(ProductoRepository repository) {
        this.repository = repository;
    }

    public Optional<Producto> ejecutar(String codigoProducto) {
        return repository.buscarPorId(codigoProducto);
    }
}
