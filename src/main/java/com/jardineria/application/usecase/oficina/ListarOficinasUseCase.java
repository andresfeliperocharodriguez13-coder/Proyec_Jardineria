package com.jardineria.application.usecase.oficina;

import com.jardineria.domain.model.Oficina;
import com.jardineria.domain.model.repository.OficinaRepository;
import java.util.List;

public class ListarOficinasUseCase {

    private final OficinaRepository oficinaRepository;

    public ListarOficinasUseCase(OficinaRepository oficinaRepository) {
        this.oficinaRepository = oficinaRepository;
    }

    public List<Oficina> ejecutar() {
        return oficinaRepository.listarTodos();
    }
}
