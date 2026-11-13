package com.jardineria.application.usecase.detallepedido;

import com.jardineria.domain.model.DetallePedido;
import com.jardineria.domain.model.repository.DetallePedidoRepository;

import java.util.Optional;

public class BuscarDetallePedidoUseCase {

    private final DetallePedidoRepository repository;

    public BuscarDetallePedidoUseCase(DetallePedidoRepository repository) {
        this.repository = repository;
    }

    public Optional<DetallePedido> ejecutar(
            int codigoPedido,
            String codigoProducto) {

        return repository.buscarPorId(codigoPedido, codigoProducto);
    }
}