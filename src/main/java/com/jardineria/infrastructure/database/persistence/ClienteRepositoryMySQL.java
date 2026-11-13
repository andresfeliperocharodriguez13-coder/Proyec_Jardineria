package com.jardineria.infrastructure.database.persistence;

import com.jardineria.domain.model.Cliente;
import com.jardineria.domain.model.repository.ClienteRepository;
import com.jardineria.infrastructure.database.ConnectionFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteRepositoryMySQL implements ClienteRepository {

    @Override
    public void guardar(Cliente cliente) {
        String sql = """
            INSERT INTO cliente (
                codigo_cliente, nombre_cliente, nombre_contacto,
                apellido_contacto, telefono, fax, linea_direccion1,
                linea_direccion2, ciudad, region, pais, codigo_postal,
                codigo_empleado_rep_ventas, limite_credito
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cliente.getCodigoCliente());
            stmt.setString(2, cliente.getNombreCliente());
            stmt.setString(3, cliente.getNombreContacto());
            stmt.setString(4, cliente.getApellidoContacto());
            stmt.setString(5, cliente.getTelefono());
            stmt.setString(6, cliente.getFax());
            stmt.setString(7, cliente.getLineaDireccion1());
            stmt.setString(8, cliente.getLineaDireccion2());
            stmt.setString(9, cliente.getCiudad());
            stmt.setString(10, cliente.getRegion());
            stmt.setString(11, cliente.getPais());
            stmt.setString(12, cliente.getCodigoPostal());

            if (cliente.getCodigoEmpleadoRepVentas() == null) {
                stmt.setNull(13, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(13, cliente.getCodigoEmpleadoRepVentas());
            }

            stmt.setBigDecimal(14, cliente.getLimiteCredito());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el cliente", e);
        }
    }

    @Override
    public List<Cliente> listarTodos() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente";

        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                clientes.add(mapearCliente(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar clientes", e);
        }

        return clientes;
    }

    @Override
    public Optional<Cliente> buscarPorId(int codigoCliente) {
        String sql = "SELECT * FROM cliente WHERE codigo_cliente = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigoCliente);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearCliente(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cliente", e);
        }

        return Optional.empty();
    }

    @Override
    public void actualizar(Cliente cliente) {
        String sql = """
            UPDATE cliente SET
                nombre_cliente = ?, nombre_contacto = ?,
                apellido_contacto = ?, telefono = ?, fax = ?,
                linea_direccion1 = ?, linea_direccion2 = ?,
                ciudad = ?, region = ?, pais = ?, codigo_postal = ?,
                codigo_empleado_rep_ventas = ?, limite_credito = ?
            WHERE codigo_cliente = ?
            """;

        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNombreCliente());
            stmt.setString(2, cliente.getNombreContacto());
            stmt.setString(3, cliente.getApellidoContacto());
            stmt.setString(4, cliente.getTelefono());
            stmt.setString(5, cliente.getFax());
            stmt.setString(6, cliente.getLineaDireccion1());
            stmt.setString(7, cliente.getLineaDireccion2());
            stmt.setString(8, cliente.getCiudad());
            stmt.setString(9, cliente.getRegion());
            stmt.setString(10, cliente.getPais());
            stmt.setString(11, cliente.getCodigoPostal());

            if (cliente.getCodigoEmpleadoRepVentas() == null) {
                stmt.setNull(12, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(12, cliente.getCodigoEmpleadoRepVentas());
            }

            stmt.setBigDecimal(13, cliente.getLimiteCredito());
            stmt.setInt(14, cliente.getCodigoCliente());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el cliente", e);
        }
    }

    @Override
    public void eliminarPorId(int codigoCliente) {
        String sql = "DELETE FROM cliente WHERE codigo_cliente = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigoCliente);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el cliente", e);
        }
    }

    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        return new Cliente(
            rs.getInt("codigo_cliente"),
            rs.getString("nombre_cliente"),
            rs.getString("nombre_contacto"),
            rs.getString("apellido_contacto"),
            rs.getString("telefono"),
            rs.getString("fax"),
            rs.getString("linea_direccion1"),
            rs.getString("linea_direccion2"),
            rs.getString("ciudad"),
            rs.getString("region"),
            rs.getString("pais"),
            rs.getString("codigo_postal"),
            (Integer) rs.getObject("codigo_empleado_rep_ventas"),
            rs.getBigDecimal("limite_credito")
        );
    }
}
