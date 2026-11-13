package com.jardineria.application.usecase.empleado;

import com.jardineria.domain.model.Empleado;
import com.jardineria.domain.model.repository.EmpleadoRepository;
import java.util.Optional;

public class BuscarEmpleadoUseCase {

    private final EmpleadoRepository empleadoRepository;

    public BuscarEmpleadoUseCase(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    public Optional<Empleado> ejecutar(int codigoEmpleado) {
        return empleadoRepository.buscarPorId(codigoEmpleado);
    }
}
