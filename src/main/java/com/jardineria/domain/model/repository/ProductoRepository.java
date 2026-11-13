package com.jardineria.domain.model.repository;

import com.jardineria.domain.model.Producto;
import java.util.List;
import java.util.Optional;

public interface ProductoRepository {

    void guardar(Producto producto);

    List<Producto> listarTodos();

    Optional<Producto> buscarPorId(String codigoProducto);

    void actualizar(Producto producto);

    void eliminarPorId(String codigoProducto);
}