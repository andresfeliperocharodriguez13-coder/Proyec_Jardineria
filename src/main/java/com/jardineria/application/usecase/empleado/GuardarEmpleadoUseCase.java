package com.jardineria.application.usecase.empleado;

import com.jardineria.domain.model.Empleado;
import com.jardineria.domain.model.repository.EmpleadoRepository;

public class GuardarEmpleadoUseCase {

    private final EmpleadoRepository empleadoRepository;

    public GuardarEmpleadoUseCase(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    public void ejecutar(Empleado empleado) {
        empleadoRepository.guardar(empleado);
    }
}
