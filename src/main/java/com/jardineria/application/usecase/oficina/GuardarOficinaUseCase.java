package com.jardineria.application.usecase.oficina;

import com.jardineria.domain.model.Oficina;
import com.jardineria.domain.model.repository.OficinaRepository;

public class GuardarOficinaUseCase {

    private final OficinaRepository oficinaRepository;

    public GuardarOficinaUseCase(OficinaRepository oficinaRepository) {
        this.oficinaRepository = oficinaRepository;
    }

    public void ejecutar(Oficina oficina) {
        oficinaRepository.guardar(oficina);
    }
}
