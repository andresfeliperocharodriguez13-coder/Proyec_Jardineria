package com.jardineria.application.usecase.empleado;

import com.jardineria.domain.model.Empleado;
import com.jardineria.domain.model.repository.EmpleadoRepository;
import java.util.List;

public class ListarEmpleadosUseCase {

    private final EmpleadoRepository empleadoRepository;

    public ListarEmpleadosUseCase(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    public List<Empleado> ejecutar() {
        return empleadoRepository.listarTodos();
    }
}
