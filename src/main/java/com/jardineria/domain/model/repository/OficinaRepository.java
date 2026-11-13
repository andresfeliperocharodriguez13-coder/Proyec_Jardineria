package com.jardineria.domain.model.repository;

import com.jardineria.domain.model.Oficina;
import java.util.List;
import java.util.Optional;

public interface OficinaRepository {

    void guardar(Oficina oficina);

    List<Oficina> listarTodos();

    Optional<Oficina> buscarPorId(String codigoOficina);

    void actualizar(Oficina oficina);

    void eliminarPorId(String codigoOficina);
}
