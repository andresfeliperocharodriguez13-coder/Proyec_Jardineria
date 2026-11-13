package com.jardineria.application.usecase.oficina;

import com.jardineria.domain.model.Oficina;
import com.jardineria.domain.model.repository.OficinaRepository;
import java.util.Optional;

public class BuscarOficinaUseCase {

    private final OficinaRepository oficinaRepository;

    public BuscarOficinaUseCase(OficinaRepository oficinaRepository) {
        this.oficinaRepository = oficinaRepository;
    }

    public Optional<Oficina> ejecutar(String codigoOficina) {
        return oficinaRepository.buscarPorId(codigoOficina);
    }
}
