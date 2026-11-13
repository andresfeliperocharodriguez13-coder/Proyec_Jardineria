package com.jardineria.infrastructure.database.persistence;

import com.jardineria.domain.model.Empleado;
import com.jardineria.domain.model.repository.EmpleadoRepository;
import com.jardineria.infrastructure.database.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmpleadoRepositoryMySQL implements EmpleadoRepository {

    @Override
    public void guardar(Empleado empleado) {
        String sql = """
            INSERT INTO empleado (
                codigo_empleado, nombre, apellido1, apellido2,
                extension, email, codigo_oficina, codigo_jefe, puesto
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, empleado.getCodigoEmpleado());
            stmt.setString(2, empleado.getNombre());
            stmt.setString(3, empleado.getApellido1());
            stmt.setString(4, empleado.getApellido2());
            stmt.setString(5, empleado.getExtension());
            stmt.setString(6, empleado.getEmail());
            stmt.setString(7, empleado.getCodigoOficina());

            if (empleado.getCodigoJefe() == null) {
                stmt.setNull(8, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(8, empleado.getCodigoJefe());
            }

            stmt.setString(9, empleado.getPuesto());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el empleado", e);
        }
    }

    @Override
    public List<Empleado> listarTodos() {
        List<Empleado> empleados = new ArrayList<>();
        String sql = "SELECT * FROM empleado";

        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                empleados.add(mapearEmpleado(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar empleados", e);
        }

        return empleados;
    }

    @Override
    public Optional<Empleado> buscarPorId(int codigoEmpleado) {
        String sql = "SELECT * FROM empleado WHERE codigo_empleado = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigoEmpleado);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearEmpleado(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar empleado", e);
        }

        return Optional.empty();
    }

    @Override
    public void actualizar(Empleado empleado) {
        String sql = """
            UPDATE empleado SET
                nombre = ?, apellido1 = ?, apellido2 = ?,
                extension = ?, email = ?, codigo_oficina = ?,
                codigo_jefe = ?, puesto = ?
            WHERE codigo_empleado = ?
            """;

        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, empleado.getNombre());
            stmt.setString(2, empleado.getApellido1());
            stmt.setString(3, empleado.getApellido2());
            stmt.setString(4, empleado.getExtension());
            stmt.setString(5, empleado.getEmail());
            stmt.setString(6, empleado.getCodigoOficina());

            if (empleado.getCodigoJefe() == null) {
                stmt.setNull(7, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(7, empleado.getCodigoJefe());
            }

            stmt.setString(8, empleado.getPuesto());
            stmt.setInt(9, empleado.getCodigoEmpleado());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar empleado", e);
        }
    }

    @Override
    public void eliminarPorId(int codigoEmpleado) {
        String sql = "DELETE FROM empleado WHERE codigo_empleado = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigoEmpleado);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar empleado", e);
        }
    }

    private Empleado mapearEmpleado(ResultSet rs) throws SQLException {
        return new Empleado(
            rs.getInt("codigo_empleado"),
            rs.getString("nombre"),
            rs.getString("apellido1"),
            rs.getString("apellido2"),
            rs.getString("extension"),
            rs.getString("email"),
            rs.getString("codigo_oficina"),
            (Integer) rs.getObject("codigo_jefe"),
            rs.getString("puesto")
        );
    }
}
