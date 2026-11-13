package com.jardineria.application.usecase.empleado;

import com.jardineria.domain.model.repository.EmpleadoRepository;

public class EliminarEmpleadoUseCase {

    private final EmpleadoRepository empleadoRepository;

    public EliminarEmpleadoUseCase(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    public void ejecutar(int codigoEmpleado) {
        empleadoRepository.eliminarPorId(codigoEmpleado);
    }
}
