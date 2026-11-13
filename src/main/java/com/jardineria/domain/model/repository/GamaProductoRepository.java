package com.jardineria.domain.model.repository;

import com.jardineria.domain.model.GamaProducto;
import java.util.List;
import java.util.Optional;

public interface GamaProductoRepository {

    void guardar(GamaProducto gamaProducto);

    List<GamaProducto> listarTodos();

    Optional<GamaProducto> buscarPorId(String gama);

    void actualizar(GamaProducto gamaProducto);

    void eliminarPorId(String gama);
}
