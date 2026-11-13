package com.jardineria.domain.model.repository;

import com.jardineria.domain.model.Cliente;
import java.util.List;
import java.util.Optional;

public interface ClienteRepository {

    void guardar(Cliente cliente);

    List<Cliente> listarTodos();

    Optional<Cliente> buscarPorId(int codigoCliente);

    void actualizar(Cliente cliente);

    void eliminarPorId(int codigoCliente);
}
