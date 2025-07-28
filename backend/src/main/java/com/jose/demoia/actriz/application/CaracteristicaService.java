package com.jose.demoia.actriz.application;

import com.jose.demoia.actriz.domain.model.Caracteristica;
import com.jose.demoia.actriz.domain.model.TipoCaracteristica;
import com.jose.demoia.actriz.domain.ports.in.CaracteristicaUseCase;
import com.jose.demoia.actriz.domain.ports.out.CaracteristicaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CaracteristicaService implements CaracteristicaUseCase {

    private final CaracteristicaRepository caracteristicaRepository;

    public CaracteristicaService(CaracteristicaRepository caracteristicaRepository) {
        this.caracteristicaRepository = caracteristicaRepository;
    }

    @Override
    public Caracteristica crearCaracteristica(Caracteristica caracteristica) {
        return caracteristicaRepository.save(caracteristica);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Caracteristica> obtenerCaracteristicaPorId(Long id) {
        return caracteristicaRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Caracteristica> obtenerTodasLasCaracteristicas() {
        return caracteristicaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Caracteristica> obtenerCaracteristicasPorTipo(TipoCaracteristica tipo) {
        return caracteristicaRepository.findByTipo(tipo);
    }

    @Override
    public Caracteristica actualizarCaracteristica(Caracteristica caracteristica) {
        return caracteristicaRepository.save(caracteristica);
    }

    @Override
    public void eliminarCaracteristica(Long id) {
        caracteristicaRepository.deleteById(id);
    }
}
