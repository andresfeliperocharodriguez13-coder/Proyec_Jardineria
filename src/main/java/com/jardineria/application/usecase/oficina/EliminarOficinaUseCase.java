package com.jardineria.application.usecase.oficina;

import com.jardineria.domain.model.repository.OficinaRepository;

public class EliminarOficinaUseCase {

    private final OficinaRepository oficinaRepository;

    public EliminarOficinaUseCase(OficinaRepository oficinaRepository) {
        this.oficinaRepository = oficinaRepository;
    }

    public void ejecutar(String codigoOficina) {
        oficinaRepository.eliminarPorId(codigoOficina);
    }
}
