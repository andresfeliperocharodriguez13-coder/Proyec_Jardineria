package com.jardineria.application.usecase.detallepedido;

import com.jardineria.domain.model.repository.DetallePedidoRepository;

public class EliminarDetallePedidoUseCase {

    private final DetallePedidoRepository repository;

    public EliminarDetallePedidoUseCase(DetallePedidoRepository repository) {
        this.repository = repository;
    }

    public void ejecutar(int codigoPedido, String codigoProducto) {
        repository.eliminarPorId(codigoPedido, codigoProducto);
    }
}