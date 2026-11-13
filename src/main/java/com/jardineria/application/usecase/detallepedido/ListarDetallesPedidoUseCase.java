package com.jardineria.application.usecase.detallepedido;

import com.jardineria.domain.model.DetallePedido;
import com.jardineria.domain.model.repository.DetallePedidoRepository;

import java.util.List;

public class ListarDetallesPedidoUseCase {

    private final DetallePedidoRepository repository;

    public ListarDetallesPedidoUseCase(DetallePedidoRepository repository) {
        this.repository = repository;
    }

    public List<DetallePedido> ejecutar() {
        return repository.listarTodos();
    }
}