package com.jardineria.infrastructure.persistence;

import com.jardineria.domain.model.GamaProducto;
import com.jardineria.domain.model.repository.GamaProductoRepository;
import com.jardineria.infrastructure.database.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GamaProductoRepositoryMySQL implements GamaProductoRepository {

    @Override
    public void guardar(GamaProducto gamaProducto) {
        String sql = "INSERT INTO gama_producto "
                + "(gama, descripcion_texto, descripcion_html, imagen) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection connection = ConnectionFactory.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, gamaProducto.getGama());
            statement.setString(2, gamaProducto.getDescripcionTexto());
            statement.setString(3, gamaProducto.getDescripcionHtml());
            statement.setString(4, gamaProducto.getImagen());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar la gama de producto", e);
        }
    }

    @Override
    public List<GamaProducto> listarTodos() {
        String sql = "SELECT * FROM gama_producto";
        List<GamaProducto> gamas = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                gamas.add(mapearGamaProducto(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar las gamas de productos", e);
        }

        return gamas;
    }

    @Override
    public Optional<GamaProducto> buscarPorId(String gama) {
        String sql = "SELECT * FROM gama_producto WHERE gama = ?";

        try (Connection connection = ConnectionFactory.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, gama);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapearGamaProducto(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar la gama de producto", e);
        }

        return Optional.empty();
    }

    @Override
    public void actualizar(GamaProducto gamaProducto) {
        String sql = "UPDATE gama_producto SET "
                + "descripcion_texto = ?, descripcion_html = ?, imagen = ? "
                + "WHERE gama = ?";

        try (Connection connection = ConnectionFactory.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, gamaProducto.getDescripcionTexto());
            statement.setString(2, gamaProducto.getDescripcionHtml());
            statement.setString(3, gamaProducto.getImagen());
            statement.setString(4, gamaProducto.getGama());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar la gama de producto", e);
        }
    }

    @Override
    public void eliminarPorId(String gama) {
        String sql = "DELETE FROM gama_producto WHERE gama = ?";

        try (Connection connection = ConnectionFactory.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, gama);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar la gama de producto", e);
        }
    }

    private GamaProducto mapearGamaProducto(ResultSet resultSet) throws SQLException {
        return new GamaProducto(
                resultSet.getString("gama"),
                resultSet.getString("descripcion_texto"),
                resultSet.getString("descripcion_html"),
                resultSet.getString("imagen")
        );
    }
}
