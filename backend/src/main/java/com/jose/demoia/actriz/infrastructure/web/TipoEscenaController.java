package com.jose.demoia.actriz.infrastructure.web;

import com.jose.demoia.actriz.domain.model.TipoEscena;
import com.jose.demoia.actriz.domain.ports.in.TipoEscenaUseCase;
import com.jose.demoia.actriz.infrastructure.web.dto.TipoEscenaRequestDto;
import com.jose.demoia.actriz.infrastructure.web.dto.TipoEscenaResponseDto;
import com.jose.demoia.actriz.infrastructure.web.mapper.TipoEscenaMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tipos-escena")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@Tag(name = "Tipos de Escena", description = "API para gestión de tipos de escena")
public class TipoEscenaController {
    
    private final TipoEscenaUseCase tipoEscenaUseCase;
    private final TipoEscenaMapper tipoEscenaMapper;
    
    public TipoEscenaController(TipoEscenaUseCase tipoEscenaUseCase, TipoEscenaMapper tipoEscenaMapper) {
        this.tipoEscenaUseCase = tipoEscenaUseCase;
        this.tipoEscenaMapper = tipoEscenaMapper;
    }
    
    @PostMapping
    @Operation(summary = "Crear un nuevo tipo de escena", description = "Crea un nuevo tipo de escena")
    public ResponseEntity<TipoEscenaResponseDto> crearTipoEscena(@Valid @RequestBody TipoEscenaRequestDto requestDto) {
        try {
            TipoEscena tipoEscena = tipoEscenaMapper.toEntity(requestDto);
            TipoEscena nuevoTipoEscena = tipoEscenaUseCase.crearTipoEscena(tipoEscena);
            TipoEscenaResponseDto responseDto = tipoEscenaMapper.toResponseDto(nuevoTipoEscena);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtener tipo de escena por ID", description = "Obtiene los detalles de un tipo de escena específico")
    public ResponseEntity<TipoEscenaResponseDto> obtenerTipoEscenaPorId(@PathVariable Long id) {
        Optional<TipoEscena> tipoEscena = tipoEscenaUseCase.obtenerTipoEscenaPorId(id);
        return tipoEscena.map(tipoEscenaMapper::toResponseDto)
                        .map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    @Operation(summary = "Obtener todos los tipos de escena", description = "Obtiene una lista de todos los tipos de escena registrados")
    public ResponseEntity<List<TipoEscenaResponseDto>> obtenerTodosLosTiposEscena() {
        List<TipoEscena> tiposEscena = tipoEscenaUseCase.obtenerTodosLosTiposEscena();
        List<TipoEscenaResponseDto> responseDtos = tiposEscena.stream()
                .map(tipoEscenaMapper::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar tipo de escena", description = "Actualiza un tipo de escena existente")
    public ResponseEntity<TipoEscenaResponseDto> actualizarTipoEscena(@PathVariable Long id,
                                                                     @Valid @RequestBody TipoEscenaRequestDto requestDto) {
        Optional<TipoEscena> tipoEscenaExistente = tipoEscenaUseCase.obtenerTipoEscenaPorId(id);
        if (tipoEscenaExistente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        TipoEscena tipoEscena = tipoEscenaExistente.get();
        tipoEscenaMapper.updateEntityFromDto(requestDto, tipoEscena);
        TipoEscena tipoEscenaActualizado = tipoEscenaUseCase.actualizarTipoEscena(tipoEscena);
        TipoEscenaResponseDto responseDto = tipoEscenaMapper.toResponseDto(tipoEscenaActualizado);
        return ResponseEntity.ok(responseDto);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar tipo de escena", description = "Elimina un tipo de escena")
    public ResponseEntity<Void> eliminarTipoEscena(@PathVariable Long id) {
        Optional<TipoEscena> tipoEscena = tipoEscenaUseCase.obtenerTipoEscenaPorId(id);
        if (tipoEscena.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        tipoEscenaUseCase.eliminarTipoEscena(id);
        return ResponseEntity.noContent().build();
    }
}
