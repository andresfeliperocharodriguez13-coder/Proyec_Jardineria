package com.jardineria.application.usecase.detallepedido;

import com.jardineria.domain.model.DetallePedido;
import com.jardineria.domain.model.repository.DetallePedidoRepository;

public class GuardarDetallePedidoUseCase {

    private final DetallePedidoRepository repository;

    public GuardarDetallePedidoUseCase(DetallePedidoRepository repository) {
        this.repository = repository;
    }

    public void ejecutar(DetallePedido detalle) {

        if (detalle.getCantidad() <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero"
            );
        }

        if (detalle.getPrecioUnidad() == null
                || detalle.getPrecioUnidad().signum() <= 0) {
            throw new IllegalArgumentException(
                    "El precio debe ser mayor que cero"
            );
        }

        repository.guardarConControlStock(detalle);
    }
}