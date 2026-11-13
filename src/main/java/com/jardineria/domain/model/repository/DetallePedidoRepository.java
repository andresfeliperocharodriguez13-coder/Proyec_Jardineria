package com.jardineria.domain.model.repository;

import com.jardineria.domain.model.DetallePedido;
import java.util.List;
import java.util.Optional;

public interface DetallePedidoRepository {

    void guardar(DetallePedido detalle);

    void guardarConControlStock(DetallePedido detalle);

    List<DetallePedido> listarTodos();

    Optional<DetallePedido> buscarPorId(
            int codigoPedido,
            String codigoProducto
    );

    void actualizar(DetallePedido detalle);

    void eliminarPorId(
            int codigoPedido,
            String codigoProducto
    );
}