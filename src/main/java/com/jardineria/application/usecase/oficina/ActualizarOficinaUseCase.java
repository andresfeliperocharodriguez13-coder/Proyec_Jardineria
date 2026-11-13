package com.jardineria.application.usecase.oficina;

import com.jardineria.domain.model.Oficina;
import com.jardineria.domain.model.repository.OficinaRepository;

public class ActualizarOficinaUseCase {

    private final OficinaRepository oficinaRepository;

    public ActualizarOficinaUseCase(OficinaRepository oficinaRepository) {
        this.oficinaRepository = oficinaRepository;
    }

    public void ejecutar(Oficina oficina) {
        oficinaRepository.actualizar(oficina);
    }
}
