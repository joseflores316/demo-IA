package com.jose.demoia.actriz.infrastructure.web;

import com.jose.demoia.actriz.domain.model.Caracteristica;
import com.jose.demoia.actriz.domain.model.TipoCaracteristica;
import com.jose.demoia.actriz.domain.ports.in.CaracteristicaUseCase;
import com.jose.demoia.actriz.infrastructure.web.dto.CaracteristicaDto;
import com.jose.demoia.actriz.infrastructure.web.mapper.CaracteristicaMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/caracteristicas")
public class CaracteristicaController {

    private final CaracteristicaUseCase caracteristicaUseCase;
    private final CaracteristicaMapper caracteristicaMapper;

    public CaracteristicaController(CaracteristicaUseCase caracteristicaUseCase, CaracteristicaMapper caracteristicaMapper) {
        this.caracteristicaUseCase = caracteristicaUseCase;
        this.caracteristicaMapper = caracteristicaMapper;
    }

    @PostMapping
    public ResponseEntity<CaracteristicaDto> crearCaracteristica(@RequestBody CaracteristicaDto caracteristicaDto) {
        Caracteristica caracteristica = caracteristicaMapper.toEntity(caracteristicaDto);
        Caracteristica nuevaCaracteristica = caracteristicaUseCase.crearCaracteristica(caracteristica);
        CaracteristicaDto responseDto = caracteristicaMapper.toDto(nuevaCaracteristica);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CaracteristicaDto> obtenerCaracteristicaPorId(@PathVariable Long id) {
        Optional<Caracteristica> caracteristica = caracteristicaUseCase.obtenerCaracteristicaPorId(id);
        return caracteristica.map(caracteristicaMapper::toDto)
                           .map(ResponseEntity::ok)
                           .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<CaracteristicaDto>> obtenerTodasLasCaracteristicas() {
        List<Caracteristica> caracteristicas = caracteristicaUseCase.obtenerTodasLasCaracteristicas();
        List<CaracteristicaDto> responseDtos = caracteristicas.stream()
                .map(caracteristicaMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<CaracteristicaDto>> obtenerCaracteristicasPorTipo(@PathVariable TipoCaracteristica tipo) {
        List<Caracteristica> caracteristicas = caracteristicaUseCase.obtenerCaracteristicasPorTipo(tipo);
        List<CaracteristicaDto> responseDtos = caracteristicas.stream()
                .map(caracteristicaMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CaracteristicaDto> actualizarCaracteristica(@PathVariable Long id, @RequestBody CaracteristicaDto caracteristicaDto) {
        Optional<Caracteristica> caracteristicaExistente = caracteristicaUseCase.obtenerCaracteristicaPorId(id);
        if (caracteristicaExistente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Caracteristica caracteristica = caracteristicaExistente.get();
        caracteristicaMapper.updateEntityFromDto(caracteristicaDto, caracteristica);
        Caracteristica caracteristicaActualizada = caracteristicaUseCase.actualizarCaracteristica(caracteristica);
        CaracteristicaDto responseDto = caracteristicaMapper.toDto(caracteristicaActualizada);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCaracteristica(@PathVariable Long id) {
        caracteristicaUseCase.eliminarCaracteristica(id);
        return ResponseEntity.noContent().build();
    }
}
