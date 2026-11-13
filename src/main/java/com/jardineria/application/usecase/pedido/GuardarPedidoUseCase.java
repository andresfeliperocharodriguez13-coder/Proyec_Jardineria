
package com.jardineria.application.usecase.pedido;

import com.jardineria.domain.model.Pedido;
import com.jardineria.domain.model.repository.PedidoRepository;

public class GuardarPedidoUseCase {

    private final PedidoRepository repository;

    public GuardarPedidoUseCase(PedidoRepository repository) {
        this.repository = repository;
    }

    public void ejecutar(Pedido pedido) {
        repository.guardar(pedido);
    }
}
