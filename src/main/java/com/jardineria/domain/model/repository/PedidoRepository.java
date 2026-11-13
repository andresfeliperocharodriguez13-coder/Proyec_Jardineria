
package com.jardineria.domain.model.repository;

import com.jardineria.domain.model.Pedido;
import java.util.List;
import java.util.Optional;

public interface PedidoRepository {

    void guardar(Pedido pedido);

    List<Pedido> listarTodos();

    Optional<Pedido> buscarPorId(int codigoPedido);

    void actualizar(Pedido pedido);

    void eliminarPorId(int codigoPedido);
}
