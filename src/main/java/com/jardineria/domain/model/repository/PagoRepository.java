package com.jardineria.domain.model.repository;

import com.jardineria.domain.model.Pago;
import java.util.List;
import java.util.Optional;

public interface PagoRepository {

    void guardar(Pago pago);

    List<Pago> listarTodos();

    Optional<Pago> buscarPorId(
            int codigoCliente,
            String idTransaccion
    );

    void actualizar(Pago pago);

    void eliminarPorId(
            int codigoCliente,
            String idTransaccion
    );
}