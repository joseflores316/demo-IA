package com.jose.demoia.actriz.infrastructure.web;

import com.jose.demoia.actriz.domain.model.Actriz;
import com.jose.demoia.actriz.domain.ports.in.ActrizUseCase;
import com.jose.demoia.actriz.domain.ports.out.FileStoragePort;
import com.jose.demoia.actriz.infrastructure.web.dto.ActrizRequestDto;
import com.jose.demoia.actriz.infrastructure.web.dto.ActrizResponseDto;
import com.jose.demoia.actriz.infrastructure.web.mapper.ActrizMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/actrices")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@Tag(name = "Actrices", description = "API para gestión de actrices")
public class ActrizController {

    private final ActrizUseCase actrizUseCase;
    private final ActrizMapper actrizMapper;
    private final FileStoragePort fileStoragePort;
    private final ObjectMapper objectMapper;

    public ActrizController(ActrizUseCase actrizUseCase, ActrizMapper actrizMapper,
                           FileStoragePort fileStoragePort, ObjectMapper objectMapper) {
        this.actrizUseCase = actrizUseCase;
        this.actrizMapper = actrizMapper;
        this.fileStoragePort = fileStoragePort;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    @Operation(summary = "Crear una nueva actriz", description = "Crea una nueva actriz sin imagen")
    public ResponseEntity<ActrizResponseDto> crearActriz(@Valid @RequestBody ActrizRequestDto requestDto) {
        Actriz actriz = actrizMapper.toEntity(requestDto);
        Actriz nuevaActriz = actrizUseCase.crearActriz(actriz);
        ActrizResponseDto responseDto = actrizMapper.toResponseDto(nuevaActriz);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PostMapping(value = "/con-imagen", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Crear actriz con imagen", description = "Crea una nueva actriz con una imagen asociada")
    public ResponseEntity<ActrizResponseDto> crearActrizConImagen(
            @RequestPart("actriz") String actrizJson,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {

        try {
            ActrizRequestDto requestDto = objectMapper.readValue(actrizJson, ActrizRequestDto.class);

            // Si hay una imagen, guardarla y asignar la URL
            if (imagen != null && !imagen.isEmpty()) {
                String imagenUrl = fileStoragePort.guardarImagen(imagen, "actrices");
                requestDto.setImagenUrl(imagenUrl);
            }

            Actriz actriz = actrizMapper.toEntity(requestDto);
            Actriz nuevaActriz = actrizUseCase.crearActriz(actriz);
            ActrizResponseDto responseDto = actrizMapper.toResponseDto(nuevaActriz);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener actriz por ID", description = "Obtiene los detalles de una actriz específica")
    public ResponseEntity<ActrizResponseDto> obtenerActrizPorId(@PathVariable Long id) {
        Optional<Actriz> actriz = actrizUseCase.obtenerActrizPorId(id);
        return actriz.map(actrizMapper::toResponseDto)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Obtener todas las actrices", description = "Obtiene una lista de todas las actrices registradas")
    public ResponseEntity<List<ActrizResponseDto>> obtenerTodasLasActrices() {
        List<Actriz> actrices = actrizUseCase.obtenerTodasLasActrices();
        List<ActrizResponseDto> responseDtos = actrices.stream()
                .map(actrizMapper::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/pais/{paisId}")
    @Operation(summary = "Obtener actrices por país", description = "Obtiene todas las actrices de un país específico")
    public ResponseEntity<List<ActrizResponseDto>> obtenerActricesPorPais(@PathVariable Long paisId) {
        List<Actriz> actrices = actrizUseCase.obtenerActricesPorPais(paisId);
        List<ActrizResponseDto> responseDtos = actrices.stream()
                .map(actrizMapper::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar actriz", description = "Actualiza una actriz sin cambiar la imagen")
    public ResponseEntity<ActrizResponseDto> actualizarActriz(@PathVariable Long id,
                                                            @Valid @RequestBody ActrizRequestDto requestDto) {
        Optional<Actriz> actrizExistente = actrizUseCase.obtenerActrizPorId(id);
        if (actrizExistente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Actriz actriz = actrizExistente.get();
        String imagenUrlAnterior = actriz.getImagenUrl(); // Guardamos la imagen anterior
        actrizMapper.updateEntityFromDto(requestDto, actriz);
        actriz.setImagenUrl(imagenUrlAnterior); // Restauramos la imagen anterior
        Actriz actrizActualizada = actrizUseCase.crearActriz(actriz);
        ActrizResponseDto responseDto = actrizMapper.toResponseDto(actrizActualizada);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping(value = "/{id}/con-imagen", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Actualizar actriz con imagen", description = "Actualiza una actriz incluyendo una nueva imagen")
    public ResponseEntity<ActrizResponseDto> actualizarActrizConImagen(
            @PathVariable Long id,
            @RequestPart("actriz") String actrizJson,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {

        try {
            Optional<Actriz> actrizExistente = actrizUseCase.obtenerActrizPorId(id);
            if (actrizExistente.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            ActrizRequestDto requestDto = objectMapper.readValue(actrizJson, ActrizRequestDto.class);
            Actriz actriz = actrizExistente.get();

            // Guardamos la imagen anterior por si no se proporciona una nueva
            String imagenUrlAnterior = actriz.getImagenUrl();

            actrizMapper.updateEntityFromDto(requestDto, actriz);

            // Si se proporciona una nueva imagen, la procesamos
            if (imagen != null && !imagen.isEmpty()) {
                String imagenUrl = fileStoragePort.guardarImagen(imagen, "actrices");
                actriz.setImagenUrl(imagenUrl);
            } else {
                // Si no se proporciona imagen, mantenemos la anterior
                actriz.setImagenUrl(imagenUrlAnterior);
            }

            Actriz actrizActualizada = actrizUseCase.crearActriz(actriz);
            ActrizResponseDto responseDto = actrizMapper.toResponseDto(actrizActualizada);
            return ResponseEntity.ok(responseDto);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarActriz(@PathVariable Long id) {
        Optional<Actriz> actriz = actrizUseCase.obtenerActrizPorId(id);
        if (actriz.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        actrizUseCase.eliminarActriz(id);
        return ResponseEntity.noContent().build();
    }
}
