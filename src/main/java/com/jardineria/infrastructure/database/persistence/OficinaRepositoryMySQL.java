package com.jardineria.infrastructure.persistence;

import com.jardineria.domain.model.Oficina;
import com.jardineria.domain.model.repository.OficinaRepository;
import com.jardineria.infrastructure.database.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OficinaRepositoryMySQL implements OficinaRepository {

    @Override
    public void guardar(Oficina oficina) {
        String sql = "INSERT INTO oficina VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, oficina.getCodigoOficina());
            stmt.setString(2, oficina.getCiudad());
            stmt.setString(3, oficina.getPais());
            stmt.setString(4, oficina.getRegion());
            stmt.setString(5, oficina.getCodigoPostal());
            stmt.setString(6, oficina.getTelefono());
            stmt.setString(7, oficina.getLineaDireccion1());
            stmt.setString(8, oficina.getLineaDireccion2());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar la oficina", e);
        }
    }

    @Override
    public List<Oficina> listarTodos() {
        List<Oficina> oficinas = new ArrayList<>();
        String sql = "SELECT * FROM oficina";

        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                oficinas.add(mapearOficina(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar oficinas", e);
        }

        return oficinas;
    }

    @Override
    public Optional<Oficina> buscarPorId(String codigoOficina) {
        String sql = "SELECT * FROM oficina WHERE codigo_oficina = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigoOficina);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearOficina(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar oficina", e);
        }

        return Optional.empty();
    }

    @Override
    public void actualizar(Oficina oficina) {
        String sql = """
            UPDATE oficina SET ciudad = ?, pais = ?, region = ?,
            codigo_postal = ?, telefono = ?, linea_direccion1 = ?,
            linea_direccion2 = ?
            WHERE codigo_oficina = ?
            """;

        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, oficina.getCiudad());
            stmt.setString(2, oficina.getPais());
            stmt.setString(3, oficina.getRegion());
            stmt.setString(4, oficina.getCodigoPostal());
            stmt.setString(5, oficina.getTelefono());
            stmt.setString(6, oficina.getLineaDireccion1());
            stmt.setString(7, oficina.getLineaDireccion2());
            stmt.setString(8, oficina.getCodigoOficina());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar oficina", e);
        }
    }

    @Override
    public void eliminarPorId(String codigoOficina) {
        String sql = "DELETE FROM oficina WHERE codigo_oficina = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigoOficina);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar oficina", e);
        }
    }

    private Oficina mapearOficina(ResultSet rs) throws SQLException {
        return new Oficina(
            rs.getString("codigo_oficina"),
            rs.getString("ciudad"),
            rs.getString("pais"),
            rs.getString("region"),
            rs.getString("codigo_postal"),
            rs.getString("telefono"),
            rs.getString("linea_direccion1"),
            rs.getString("linea_direccion2")
        );
    }
}
