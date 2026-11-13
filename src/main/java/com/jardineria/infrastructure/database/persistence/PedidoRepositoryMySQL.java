
package com.jardineria.infrastructure.database.persistence;

import com.jardineria.domain.model.Pedido;
import com.jardineria.domain.model.repository.PedidoRepository;
import com.jardineria.infrastructure.database.ConnectionFactory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PedidoRepositoryMySQL implements PedidoRepository {

    @Override
    public void guardar(Pedido pedido) {

        String sql = """
                INSERT INTO pedido
                (codigo_pedido, fecha_pedido, fecha_esperada,
                 fecha_entrega, estado, comentarios, codigo_cliente)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conexion = ConnectionFactory.getInstance().getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setInt(1, pedido.getCodigoPedido());
            consulta.setDate(2, Date.valueOf(pedido.getFechaPedido()));
            consulta.setDate(3, Date.valueOf(pedido.getFechaEsperada()));

            if (pedido.getFechaEntrega() != null) {
                consulta.setDate(4, Date.valueOf(pedido.getFechaEntrega()));
            } else {
                consulta.setNull(4, java.sql.Types.DATE);
            }

            consulta.setString(5, pedido.getEstado());
            consulta.setString(6, pedido.getComentarios());
            consulta.setInt(7, pedido.getCodigoCliente());

            consulta.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el pedido", e);
        }
    }

    @Override
    public List<Pedido> listarTodos() {

        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedido";

        try (Connection conexion = ConnectionFactory.getInstance().getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {

            while (resultado.next()) {
                pedidos.add(mapearPedido(resultado));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar los pedidos", e);
        }

        return pedidos;
    }

    @Override
    public Optional<Pedido> buscarPorId(int codigoPedido) {

        String sql = "SELECT * FROM pedido WHERE codigo_pedido = ?";

        try (Connection conexion = ConnectionFactory.getInstance().getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setInt(1, codigoPedido);

            try (ResultSet resultado = consulta.executeQuery()) {

                if (resultado.next()) {
                    return Optional.of(mapearPedido(resultado));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar el pedido", e);
        }

        return Optional.empty();
    }

    @Override
    public void actualizar(Pedido pedido) {

        String sql = """
                UPDATE pedido SET
                fecha_pedido = ?,
                fecha_esperada = ?,
                fecha_entrega = ?,
                estado = ?,
                comentarios = ?,
                codigo_cliente = ?
                WHERE codigo_pedido = ?
                """;

        try (Connection conexion = ConnectionFactory.getInstance().getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setDate(1, Date.valueOf(pedido.getFechaPedido()));
            consulta.setDate(2, Date.valueOf(pedido.getFechaEsperada()));

            if (pedido.getFechaEntrega() != null) {
                consulta.setDate(3, Date.valueOf(pedido.getFechaEntrega()));
            } else {
                consulta.setNull(3, java.sql.Types.DATE);
            }

            consulta.setString(4, pedido.getEstado());
            consulta.setString(5, pedido.getComentarios());
            consulta.setInt(6, pedido.getCodigoCliente());
            consulta.setInt(7, pedido.getCodigoPedido());

            consulta.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el pedido", e);
        }
    }

    @Override
    public void eliminarPorId(int codigoPedido) {

        String sql = "DELETE FROM pedido WHERE codigo_pedido = ?";

        try (Connection conexion = ConnectionFactory.getInstance().getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setInt(1, codigoPedido);
            consulta.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el pedido", e);
        }
    }

    private Pedido mapearPedido(ResultSet resultado) throws SQLException {

        Date fechaEntrega = resultado.getDate("fecha_entrega");

        return new Pedido(
                resultado.getInt("codigo_pedido"),
                resultado.getDate("fecha_pedido").toLocalDate(),
                resultado.getDate("fecha_esperada").toLocalDate(),
                fechaEntrega != null ? fechaEntrega.toLocalDate() : null,
                resultado.getString("estado"),
                resultado.getString("comentarios"),
                resultado.getInt("codigo_cliente")
        );
    }
}