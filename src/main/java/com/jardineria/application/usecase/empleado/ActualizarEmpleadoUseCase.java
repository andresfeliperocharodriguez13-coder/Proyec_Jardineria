package com.jardineria.application.usecase.empleado;

import com.jardineria.domain.model.Empleado;
import com.jardineria.domain.model.repository.EmpleadoRepository;

public class ActualizarEmpleadoUseCase {

    private final EmpleadoRepository empleadoRepository;

    public ActualizarEmpleadoUseCase(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    public void ejecutar(Empleado empleado) {
        empleadoRepository.actualizar(empleado);
    }
}
