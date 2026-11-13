
package com.jardineria.application.usecase.pedido;

import com.jardineria.domain.model.repository.PedidoRepository;

public class EliminarPedidoUseCase {

    private final PedidoRepository repository;

    public EliminarPedidoUseCase(PedidoRepository repository) {
        this.repository = repository;
    }

    public void ejecutar(int codigoPedido) {
        repository.eliminarPorId(codigoPedido);
    }
}
