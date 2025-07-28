package com.jose.demoia.actriz.application;

import com.jose.demoia.actriz.domain.model.Pais;
import com.jose.demoia.actriz.domain.ports.in.PaisUseCase;
import com.jose.demoia.actriz.domain.ports.out.PaisRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PaisService implements PaisUseCase {

    private final PaisRepository paisRepository;

    public PaisService(PaisRepository paisRepository) {
        this.paisRepository = paisRepository;
    }

    @Override
    public Pais crearPais(Pais pais) {
        return paisRepository.save(pais);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pais> obtenerPaisPorId(Long id) {
        return paisRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pais> obtenerTodosLosPaises() {
        return paisRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pais> obtenerPaisPorCodigoIso(String codigoIso) {
        return paisRepository.findByCodigoIso(codigoIso);
    }

    @Override
    public Pais actualizarPais(Pais pais) {
        return paisRepository.save(pais);
    }

    @Override
    public void eliminarPais(Long id) {
        paisRepository.deleteById(id);
    }
}
