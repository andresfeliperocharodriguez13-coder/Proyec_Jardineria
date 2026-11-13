
package com.jardineria.application.usecase.pedido;

import com.jardineria.domain.model.Pedido;
import com.jardineria.domain.model.repository.PedidoRepository;

import java.util.List;

public class ListarPedidosUseCase {

    private final PedidoRepository repository;

    public ListarPedidosUseCase(PedidoRepository repository) {
        this.repository = repository;
    }

    public List<Pedido> ejecutar() {
        return repository.listarTodos();
    }
}
