package com.jardineria.infrastructure.database.persistence;

import com.jardineria.domain.model.Pago;
import com.jardineria.domain.model.repository.PagoRepository;
import com.jardineria.infrastructure.database.ConnectionFactory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PagoRepositoryMySQL implements PagoRepository {

    @Override
    public void guardar(Pago pago) {

        String sql = """
                INSERT INTO pago
                (codigo_cliente, forma_pago, id_transaccion,
                 fecha_pago, total)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conexion = ConnectionFactory.getInstance().getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setInt(1, pago.getCodigoCliente());
            consulta.setString(2, pago.getFormaPago());
            consulta.setString(3, pago.getIdTransaccion());
            consulta.setDate(4, Date.valueOf(pago.getFechaPago()));
            consulta.setBigDecimal(5, pago.getTotal());

            consulta.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el pago", e);
        }
    }

    @Override
    public List<Pago> listarTodos() {

        List<Pago> pagos = new ArrayList<>();
        String sql = "SELECT * FROM pago";

        try (Connection conexion = ConnectionFactory.getInstance().getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {

            while (resultado.next()) {
                pagos.add(mapearPago(resultado));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar los pagos", e);
        }

        return pagos;
    }

    @Override
    public Optional<Pago> buscarPorId(
            int codigoCliente, String idTransaccion) {

        String sql = """
                SELECT * FROM pago
                WHERE codigo_cliente = ? AND id_transaccion = ?
                """;

        try (Connection conexion = ConnectionFactory.getInstance().getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setInt(1, codigoCliente);
            consulta.setString(2, idTransaccion);

            try (ResultSet resultado = consulta.executeQuery()) {

                if (resultado.next()) {
                    return Optional.of(mapearPago(resultado));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar el pago", e);
        }

        return Optional.empty();
    }

    @Override
    public void actualizar(Pago pago) {

        String sql = """
                UPDATE pago SET
                forma_pago = ?,
                fecha_pago = ?,
                total = ?
                WHERE codigo_cliente = ? AND id_transaccion = ?
                """;

        try (Connection conexion = ConnectionFactory.getInstance().getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setString(1, pago.getFormaPago());
            consulta.setDate(2, Date.valueOf(pago.getFechaPago()));
            consulta.setBigDecimal(3, pago.getTotal());
            consulta.setInt(4, pago.getCodigoCliente());
            consulta.setString(5, pago.getIdTransaccion());

            consulta.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el pago", e);
        }
    }

    @Override
    public void eliminarPorId(int codigoCliente, String idTransaccion) {

        String sql = """
                DELETE FROM pago
                WHERE codigo_cliente = ? AND id_transaccion = ?
                """;

        try (Connection conexion = ConnectionFactory.getInstance().getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setInt(1, codigoCliente);
            consulta.setString(2, idTransaccion);

            consulta.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el pago", e);
        }
    }

    private Pago mapearPago(ResultSet resultado) throws SQLException {

        return new Pago(
                resultado.getInt("codigo_cliente"),
                resultado.getString("forma_pago"),
                resultado.getString("id_transaccion"),
                resultado.getDate("fecha_pago").toLocalDate(),
                resultado.getBigDecimal("total")
        );
    }
}