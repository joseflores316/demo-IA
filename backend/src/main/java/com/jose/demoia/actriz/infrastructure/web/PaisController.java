package com.jose.demoia.actriz.infrastructure.web;

import com.jose.demoia.actriz.domain.model.Pais;
import com.jose.demoia.actriz.domain.ports.in.PaisUseCase;
import com.jose.demoia.actriz.infrastructure.web.dto.PaisDto;
import com.jose.demoia.actriz.infrastructure.web.mapper.PaisMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/paises")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class PaisController {

    private final PaisUseCase paisUseCase;
    private final PaisMapper paisMapper;

    public PaisController(PaisUseCase paisUseCase, PaisMapper paisMapper) {
        this.paisUseCase = paisUseCase;
        this.paisMapper = paisMapper;
    }

    @PostMapping
    public ResponseEntity<PaisDto> crearPais(@RequestBody PaisDto paisDto) {
        Pais pais = paisMapper.toEntity(paisDto);
        Pais nuevoPais = paisUseCase.crearPais(pais);
        PaisDto responseDto = paisMapper.toDto(nuevoPais);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaisDto> obtenerPaisPorId(@PathVariable Long id) {
        Optional<Pais> pais = paisUseCase.obtenerPaisPorId(id);
        return pais.map(paisMapper::toDto)
                  .map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<PaisDto>> obtenerTodosLosPaises() {
        List<Pais> paises = paisUseCase.obtenerTodosLosPaises();
        List<PaisDto> responseDtos = paises.stream()
                .map(paisMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/codigo/{codigoIso}")
    public ResponseEntity<PaisDto> obtenerPaisPorCodigoIso(@PathVariable String codigoIso) {
        Optional<Pais> pais = paisUseCase.obtenerPaisPorCodigoIso(codigoIso);
        return pais.map(paisMapper::toDto)
                  .map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaisDto> actualizarPais(@PathVariable Long id, @RequestBody PaisDto paisDto) {
        Optional<Pais> paisExistente = paisUseCase.obtenerPaisPorId(id);
        if (paisExistente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Pais pais = paisExistente.get();
        paisMapper.updateEntityFromDto(paisDto, pais);
        Pais paisActualizado = paisUseCase.actualizarPais(pais);
        PaisDto responseDto = paisMapper.toDto(paisActualizado);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPais(@PathVariable Long id) {
        paisUseCase.eliminarPais(id);
        return ResponseEntity.noContent().build();
    }
}
