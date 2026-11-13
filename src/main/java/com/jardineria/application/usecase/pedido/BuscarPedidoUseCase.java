
package com.jardineria.application.usecase.pedido;

import com.jardineria.domain.model.Pedido;
import com.jardineria.domain.model.repository.PedidoRepository;

import java.util.Optional;

public class BuscarPedidoUseCase {

    private final PedidoRepository repository;

    public BuscarPedidoUseCase(PedidoRepository repository) {
        this.repository = repository;
    }

    public Optional<Pedido> ejecutar(int codigoPedido) {
        return repository.buscarPorId(codigoPedido);
    }
}
