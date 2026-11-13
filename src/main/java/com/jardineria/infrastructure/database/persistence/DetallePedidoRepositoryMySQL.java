
package com.jardineria.infrastructure.database.persistence;

import com.jardineria.domain.model.DetallePedido;
import com.jardineria.domain.model.repository.DetallePedidoRepository;
import com.jardineria.infrastructure.database.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DetallePedidoRepositoryMySQL implements DetallePedidoRepository {

    @Override
    public void guardar(DetallePedido detalle) {
        guardarConControlStock(detalle);
    }

    @Override
    public void guardarConControlStock(DetallePedido detalle) {

        if (detalle.getCantidad() <= 0 ||
                detalle.getPrecioUnidad() == null ||
                detalle.getPrecioUnidad().signum() <= 0) {
            throw new IllegalArgumentException("Cantidad y precio deben ser mayores que cero");
        }

        String sqlStock = """
                UPDATE producto
                SET cantidad_en_stock = cantidad_en_stock - ?
                WHERE codigo_producto = ?
                AND cantidad_en_stock >= ?
                """;

        String sqlDetalle = """
                INSERT INTO detalle_pedido
                (codigo_pedido, codigo_producto, cantidad, precio_unidad, numero_linea)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conexion = ConnectionFactory.getInstance().getConnection()) {
            conexion.setAutoCommit(false);

            try {
                try (PreparedStatement consulta = conexion.prepareStatement(sqlStock)) {
                    consulta.setInt(1, detalle.getCantidad());
                    consulta.setString(2, detalle.getCodigoProducto());
                    consulta.setInt(3, detalle.getCantidad());

                    if (consulta.executeUpdate() == 0) {
                        throw new RuntimeException("Producto inexistente o stock insuficiente");
                    }
                }

                try (PreparedStatement consulta = conexion.prepareStatement(sqlDetalle)) {
                    consulta.setInt(1, detalle.getCodigoPedido());
                    consulta.setString(2, detalle.getCodigoProducto());
                    consulta.setInt(3, detalle.getCantidad());
                    consulta.setBigDecimal(4, detalle.getPrecioUnidad());
                    consulta.setInt(5, detalle.getNumeroLinea());
                    consulta.executeUpdate();
                }

                conexion.commit();

            } catch (Exception e) {
                conexion.rollback();
                throw new RuntimeException("Error al guardar el detalle y actualizar el stock", e);
            } finally {
                conexion.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error en la transacción del detalle", e);
        }
    }

    @Override
    public List<DetallePedido> listarTodos() {
        List<DetallePedido> detalles = new ArrayList<>();
        String sql = "SELECT * FROM detalle_pedido";

        try (Connection conexion = ConnectionFactory.getInstance().getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {

            while (resultado.next()) {
                detalles.add(mapearDetalle(resultado));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar los detalles", e);
        }

        return detalles;
    }

    @Override
    public Optional<DetallePedido> buscarPorId(int codigoPedido, String codigoProducto) {
        String sql = """
                SELECT * FROM detalle_pedido
                WHERE codigo_pedido = ? AND codigo_producto = ?
                """;

        try (Connection conexion = ConnectionFactory.getInstance().getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setInt(1, codigoPedido);
            consulta.setString(2, codigoProducto);

            try (ResultSet resultado = consulta.executeQuery()) {
                if (resultado.next()) {
                    return Optional.of(mapearDetalle(resultado));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar el detalle", e);
        }

        return Optional.empty();
    }

    @Override
    public void actualizar(DetallePedido detalle) {

        if (detalle.getCantidad() <= 0 ||
                detalle.getPrecioUnidad() == null ||
                detalle.getPrecioUnidad().signum() <= 0) {
            throw new IllegalArgumentException("Cantidad y precio deben ser mayores que cero");
        }

        String sqlBuscar = """
                SELECT cantidad FROM detalle_pedido
                WHERE codigo_pedido = ? AND codigo_producto = ?
                FOR UPDATE
                """;

        String sqlDetalle = """
                UPDATE detalle_pedido SET
                cantidad = ?, precio_unidad = ?, numero_linea = ?
                WHERE codigo_pedido = ? AND codigo_producto = ?
                """;

        try (Connection conexion = ConnectionFactory.getInstance().getConnection()) {
            conexion.setAutoCommit(false);

            try {
                int cantidadAnterior;

                try (PreparedStatement consulta = conexion.prepareStatement(sqlBuscar)) {
                    consulta.setInt(1, detalle.getCodigoPedido());
                    consulta.setString(2, detalle.getCodigoProducto());

                    try (ResultSet resultado = consulta.executeQuery()) {
                        if (!resultado.next()) {
                            throw new RuntimeException("No se encontró el detalle del pedido");
                        }
                        cantidadAnterior = resultado.getInt("cantidad");
                    }
                }

                int diferencia = detalle.getCantidad() - cantidadAnterior;

                if (diferencia != 0) {
                    String sqlStock = diferencia > 0
                            ? """
                              UPDATE producto SET cantidad_en_stock = cantidad_en_stock - ?
                              WHERE codigo_producto = ? AND cantidad_en_stock >= ?
                              """
                            : """
                              UPDATE producto SET cantidad_en_stock = cantidad_en_stock + ?
                              WHERE codigo_producto = ?
                              """;

                    try (PreparedStatement consulta = conexion.prepareStatement(sqlStock)) {
                        consulta.setInt(1, Math.abs(diferencia));
                        consulta.setString(2, detalle.getCodigoProducto());

                        if (diferencia > 0) {
                            consulta.setInt(3, diferencia);
                        }

                        if (consulta.executeUpdate() == 0) {
                            throw new RuntimeException("No hay suficiente stock o el producto no existe");
                        }
                    }
                }

                try (PreparedStatement consulta = conexion.prepareStatement(sqlDetalle)) {
                    consulta.setInt(1, detalle.getCantidad());
                    consulta.setBigDecimal(2, detalle.getPrecioUnidad());
                    consulta.setInt(3, detalle.getNumeroLinea());
                    consulta.setInt(4, detalle.getCodigoPedido());
                    consulta.setString(5, detalle.getCodigoProducto());
                    consulta.executeUpdate();
                }

                conexion.commit();

            } catch (Exception e) {
                conexion.rollback();
                throw new RuntimeException("Error al actualizar el detalle y el inventario", e);
            } finally {
                conexion.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error en la transacción de actualización", e);
        }
    }

    @Override
    public void eliminarPorId(int codigoPedido, String codigoProducto) {
        String sqlBuscar = """
                SELECT cantidad FROM detalle_pedido
                WHERE codigo_pedido = ? AND codigo_producto = ?
                FOR UPDATE
                """;

        String sqlStock = """
                UPDATE producto SET cantidad_en_stock = cantidad_en_stock + ?
                WHERE codigo_producto = ?
                """;

        String sqlEliminar = """
                DELETE FROM detalle_pedido
                WHERE codigo_pedido = ? AND codigo_producto = ?
                """;

        try (Connection conexion = ConnectionFactory.getInstance().getConnection()) {
            conexion.setAutoCommit(false);

            try {
                int cantidad;

                try (PreparedStatement consulta = conexion.prepareStatement(sqlBuscar)) {
                    consulta.setInt(1, codigoPedido);
                    consulta.setString(2, codigoProducto);

                    try (ResultSet resultado = consulta.executeQuery()) {
                        if (!resultado.next()) {
                            throw new RuntimeException("No se encontró el detalle");
                        }
                        cantidad = resultado.getInt("cantidad");
                    }
                }

                try (PreparedStatement consulta = conexion.prepareStatement(sqlStock)) {
                    consulta.setInt(1, cantidad);
                    consulta.setString(2, codigoProducto);
                    consulta.executeUpdate();
                }

                try (PreparedStatement consulta = conexion.prepareStatement(sqlEliminar)) {
                    consulta.setInt(1, codigoPedido);
                    consulta.setString(2, codigoProducto);
                    consulta.executeUpdate();
                }

                conexion.commit();

            } catch (Exception e) {
                conexion.rollback();
                throw new RuntimeException("Error al eliminar el detalle y devolver el stock", e);
            } finally {
                conexion.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error en la transacción de eliminación", e);
        }
    }

    private DetallePedido mapearDetalle(ResultSet resultado) throws SQLException {
        return new DetallePedido(
                resultado.getInt("codigo_pedido"),
                resultado.getString("codigo_producto"),
                resultado.getInt("cantidad"),
                resultado.getBigDecimal("precio_unidad"),
                resultado.getInt("numero_linea")
        );
    }
}
