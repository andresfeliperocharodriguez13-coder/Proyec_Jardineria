
package com.jardineria.infrastructure.database.persistence;

import com.jardineria.domain.model.Producto;
import com.jardineria.domain.model.repository.ProductoRepository;
import com.jardineria.infrastructure.database.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductoRepositoryMySQL implements ProductoRepository {

    @Override
    public void guardar(Producto producto) {

        String sql = "INSERT INTO producto VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConnectionFactory.getInstance().getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setString(1, producto.getCodigoProducto());
            consulta.setString(2, producto.getNombre());
            consulta.setString(3, producto.getGama());
            consulta.setString(4, producto.getDimensiones());
            consulta.setString(5, producto.getProveedor());
            consulta.setString(6, producto.getDescripcion());
            consulta.setInt(7, producto.getCantidadEnStock());
            consulta.setBigDecimal(8, producto.getPrecioVenta());
            consulta.setBigDecimal(9, producto.getPrecioProveedor());

            consulta.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el producto", e);
        }
    }

    @Override
    public List<Producto> listarTodos() {

        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM producto";

        try (Connection conexion = ConnectionFactory.getInstance().getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {

            while (resultado.next()) {
                productos.add(mapearProducto(resultado));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar los productos", e);
        }

        return productos;
    }

    @Override
    public Optional<Producto> buscarPorId(String codigoProducto) {

        String sql = "SELECT * FROM producto WHERE codigo_producto = ?";

        try (Connection conexion = ConnectionFactory.getInstance().getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setString(1, codigoProducto);

            try (ResultSet resultado = consulta.executeQuery()) {

                if (resultado.next()) {
                    return Optional.of(mapearProducto(resultado));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar el producto", e);
        }

        return Optional.empty();
    }

    @Override
    public void actualizar(Producto producto) {

        String sql = """
                UPDATE producto SET
                nombre = ?,
                gama = ?,
                dimensiones = ?,
                proveedor = ?,
                descripcion = ?,
                cantidad_en_stock = ?,
                precio_venta = ?,
                precio_proveedor = ?
                WHERE codigo_producto = ?
                """;

        try (Connection conexion = ConnectionFactory.getInstance().getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setString(1, producto.getNombre());
            consulta.setString(2, producto.getGama());
            consulta.setString(3, producto.getDimensiones());
            consulta.setString(4, producto.getProveedor());
            consulta.setString(5, producto.getDescripcion());
            consulta.setInt(6, producto.getCantidadEnStock());
            consulta.setBigDecimal(7, producto.getPrecioVenta());
            consulta.setBigDecimal(8, producto.getPrecioProveedor());
            consulta.setString(9, producto.getCodigoProducto());

            consulta.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el producto", e);
        }
    }

    @Override
    public void eliminarPorId(String codigoProducto) {

        String sql = "DELETE FROM producto WHERE codigo_producto = ?";

        try (Connection conexion = ConnectionFactory.getInstance().getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setString(1, codigoProducto);
            consulta.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el producto", e);
        }
    }

    private Producto mapearProducto(ResultSet resultado) throws SQLException {

        return new Producto(
                resultado.getString("codigo_producto"),
                resultado.getString("nombre"),
                resultado.getString("gama"),
                resultado.getString("dimensiones"),
                resultado.getString("proveedor"),
                resultado.getString("descripcion"),
                resultado.getInt("cantidad_en_stock"),
                resultado.getBigDecimal("precio_venta"),
                resultado.getBigDecimal("precio_proveedor")
        );
    }
}
