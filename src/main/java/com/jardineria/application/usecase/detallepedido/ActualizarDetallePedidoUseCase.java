package com.jardineria.application.usecase.detallepedido;

import com.jardineria.domain.model.DetallePedido;
import com.jardineria.domain.model.repository.DetallePedidoRepository;

public class ActualizarDetallePedidoUseCase {

    private final DetallePedidoRepository repository;

    public ActualizarDetallePedidoUseCase(DetallePedidoRepository repository) {
        this.repository = repository;
    }

    public void ejecutar(DetallePedido detalle) {
        repository.actualizar(detalle);
    }
}