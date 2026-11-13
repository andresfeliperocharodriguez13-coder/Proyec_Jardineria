
package com.jardineria.application.usecase.pedido;

import com.jardineria.domain.model.Pedido;
import com.jardineria.domain.model.repository.PedidoRepository;

public class ActualizarPedidoUseCase {

    private final PedidoRepository repository;

    public ActualizarPedidoUseCase(PedidoRepository repository) {
        this.repository = repository;
    }

    public void ejecutar(Pedido pedido) {
        repository.actualizar(pedido);
    }
}
