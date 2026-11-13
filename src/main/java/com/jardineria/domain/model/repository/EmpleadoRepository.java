package com.jardineria.domain.model.repository;

import com.jardineria.domain.model.Empleado;
import java.util.List;
import java.util.Optional;

public interface EmpleadoRepository {

    void guardar(Empleado empleado);

    List<Empleado> listarTodos();

    Optional<Empleado> buscarPorId(int codigoEmpleado);

    void actualizar(Empleado empleado);

    void eliminarPorId(int codigoEmpleado);
}
