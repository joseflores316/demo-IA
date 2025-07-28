package com.jose.demoia.actriz.application;

import com.jose.demoia.actriz.domain.model.Escena;
import com.jose.demoia.actriz.domain.model.ActrizEscena;
import com.jose.demoia.actriz.domain.model.Actriz;
import com.jose.demoia.actriz.domain.ports.in.EscenaUseCase;
import com.jose.demoia.actriz.domain.ports.in.ActrizEscenaUseCase;
import com.jose.demoia.actriz.domain.ports.in.ActrizUseCase;
import com.jose.demoia.actriz.domain.ports.out.EscenaRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EscenaService implements EscenaUseCase {

    private final EscenaRepositoryPort escenaRepositoryPort;
    private final ActrizEscenaUseCase actrizEscenaUseCase;
    private final ActrizUseCase actrizUseCase;

    public EscenaService(EscenaRepositoryPort escenaRepositoryPort,
                        ActrizEscenaUseCase actrizEscenaUseCase,
                        ActrizUseCase actrizUseCase) {
        this.escenaRepositoryPort = escenaRepositoryPort;
        this.actrizEscenaUseCase = actrizEscenaUseCase;
        this.actrizUseCase = actrizUseCase;
    }

    @Override
    @Transactional
    public Escena crearEscena(Escena escena) {
        return escenaRepositoryPort.save(escena);
    }

    @Transactional
    public Escena crearEscenaConActrices(Escena escena, List<ActrizEscenaInfo> actricesInfo) {
        // Primero guardar la escena
        Escena nuevaEscena = escenaRepositoryPort.save(escena);

        // Luego asociar las actrices si se proporcionaron
        if (actricesInfo != null && !actricesInfo.isEmpty()) {
            for (ActrizEscenaInfo info : actricesInfo) {
                actrizEscenaUseCase.asociarActrizConEscena(info.getActrizId(), nuevaEscena.getId(), info.getPapel());
            }
        }

        return nuevaEscena;
    }

    @Override
    public Optional<Escena> obtenerEscenaPorId(Long id) {
        return escenaRepositoryPort.findById(id);
    }

    @Override
    public List<Escena> obtenerTodasLasEscenas() {
        return escenaRepositoryPort.findAll();
    }

    @Override
    public List<Escena> obtenerEscenasPorTipo(Long tipoEscenaId) {
        return escenaRepositoryPort.findByTipoEscenaId(tipoEscenaId);
    }

    public List<Escena> obtenerEscenasPorActriz(Long actrizId) {
        return escenaRepositoryPort.findByActrizId(actrizId);
    }

    @Override
    @Transactional
    public void eliminarEscena(Long id) {
        // Primero eliminar todas las asociaciones con actrices
        actrizEscenaUseCase.eliminarTodasLasAsociacionesPorEscena(id);
        // Luego eliminar la escena
        escenaRepositoryPort.deleteById(id);
    }

    // Clase auxiliar para manejar la información de actriz-escena
    public static class ActrizEscenaInfo {
        private Long actrizId;
        private String papel;

        public ActrizEscenaInfo() {}

        public ActrizEscenaInfo(Long actrizId, String papel) {
            this.actrizId = actrizId;
            this.papel = papel;
        }

        public Long getActrizId() {
            return actrizId;
        }

        public void setActrizId(Long actrizId) {
            this.actrizId = actrizId;
        }

        public String getPapel() {
            return papel;
        }

        public void setPapel(String papel) {
            this.papel = papel;
        }
    }
}
